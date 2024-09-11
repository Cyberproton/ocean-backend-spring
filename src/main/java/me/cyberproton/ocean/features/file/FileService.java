package me.cyberproton.ocean.features.file;

import java.io.*;
import java.net.URLConnection;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.cyberproton.ocean.features.user.UserEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.transfer.s3.S3TransferManager;

@Log4j2
@AllArgsConstructor
@Service
public class FileService {
    private final S3Client s3Client;
    private final S3AsyncClient s3AsyncClient;
    private final S3TransferManager s3TransferManager;
    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;
    private final ExternalFileConfig externalFileConfig;
    private final FileRepository fileRepository;

    public FileEntity getFile(Long id) {
        return fileRepository.findById(id).orElseThrow();
    }

    public FileEntity uploadFile(
            String bucket,
            MultipartFile file,
            UserEntity owner,
            Consumer<FileEntity> fileEntityModifier) {
        try {
            InputStream inputStream = file.getInputStream();
            return uploadFileStream(
                    bucket,
                    file.getOriginalFilename(),
                    inputStream,
                    file.getContentType(),
                    file.getSize(),
                    owner,
                    fileEntityModifier);
        } catch (IOException e) {
            log.error("Failed to upload file", e);
        } finally {
            try {
                file.getInputStream().close();
            } catch (IOException e) {
                log.error("Failed to close input stream", e);
            }
        }
        return null;
    }

    public FileEntity uploadFile(
            String bucket, File file, UserEntity owner, Consumer<FileEntity> fileEntityModifier) {
        String mimetype = URLConnection.guessContentTypeFromName(file.getName());
        try {
            InputStream inputStream = new FileInputStream(file);
            return uploadFileStream(
                    bucket,
                    file.getName(),
                    inputStream,
                    mimetype,
                    file.length(),
                    owner,
                    fileEntityModifier);
        } catch (FileNotFoundException e) {
            log.error("Failed to upload file", e);
        } finally {
            try {
                new FileInputStream(file).close();
            } catch (IOException e) {
                log.error("Failed to close input stream", e);
            }
        }
        return null;
    }

    public FileEntity uploadFile(String bucket, File file, UserEntity owner) {
        return uploadFile(bucket, file, owner, null);
    }

    private FileEntity uploadFileStream(
            String bucket,
            String key,
            InputStream inputStream,
            String contentType,
            long size,
            UserEntity owner,
            Consumer<FileEntity> fileEntityModifier) {
        FileEntity entity =
                FileEntity.builder()
                        .path(bucket)
                        .name(key)
                        .size(size)
                        .mimetype(contentType)
                        .owner(owner)
                        .build();
        if (fileEntityModifier != null) {
            fileEntityModifier.accept(entity);
        }
        FileEntity res = fileRepository.save(entity);
        s3AsyncClient
                .putObject(
                        PutObjectRequest.builder()
                                .bucket(bucket)
                                .key(res.getId().toString())
                                .contentType(contentType)
                                .build(),
                        AsyncRequestBody.fromInputStream(
                                inputStream, size, threadPoolTaskExecutor.getThreadPoolExecutor()))
                .exceptionally(
                        e -> {
                            log.error("Failed to upload file", e);
                            return null;
                        });
        return res;
    }

    public FileEntity uploadFileStream(
            String bucket,
            String key,
            InputStream inputStream,
            String contentType,
            long size,
            UserEntity owner) {
        return uploadFileStream(bucket, key, inputStream, contentType, size, owner, null);
    }

    public FileEntity uploadFileToDefaultBucket(
            MultipartFile file, UserEntity owner, Consumer<FileEntity> fileEntityModifier) {
        return uploadFile(externalFileConfig.bucket(), file, owner, fileEntityModifier);
    }

    public FileEntity uploadFileToDefaultBucket(MultipartFile file, UserEntity owner) {
        return uploadFile(externalFileConfig.bucket(), file, owner, null);
    }

    public FileEntity uploadFileToDefaultBucket(
            File file, UserEntity owner, Consumer<FileEntity> fileEntityModifier) {
        return uploadFile(externalFileConfig.bucket(), file, owner, fileEntityModifier);
    }

    public FileEntity uploadFileToDefaultBucket(File file, UserEntity owner) {
        return uploadFile(externalFileConfig.bucket(), file, owner, null);
    }

    public void streamToOutputStream(StreamFileToOutputStreamRequest request) {
        FileEntity file = fileRepository.findById(request.id()).orElseThrow();
        var download =
                s3AsyncClient.getObject(
                        GetObjectRequest.builder()
                                .range(request.range())
                                .bucket(file.getPath())
                                .key(file.getId().toString())
                                .build(),
                        AsyncResponseTransformer.toBlockingInputStream());
        // Input stream from s3 will automatically close after transferTo
        try {
            var res = download.join();
            res.transferTo(request.outputStream());
            // Close the output stream after transferTo
            if (request.closeStreamAfterFinish()) {
                request.outputStream().close();
            }
            res.close();
        } catch (Exception e) {
            log.error("Failed to stream file", e);
        }
    }

    public StreamingResponseBody streamToStreamingResponseBody(StreamFileToBodyRequest request) {
        return outputStream ->
                streamToOutputStream(
                        StreamFileToOutputStreamRequest.builder()
                                .id(request.id())
                                .range(request.range())
                                .outputStream(outputStream)
                                .closeStreamAfterFinish(true)
                                .build());
    }
}
