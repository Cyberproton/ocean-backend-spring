package me.cyberproton.ocean.features.file;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.cyberproton.ocean.features.user.User;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.transfer.s3.S3TransferManager;

import java.io.InputStream;
import java.io.OutputStream;

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

    public FileEntity uploadFile(String bucket, MultipartFile file, User owner) {
        FileEntity res = fileRepository.save(
                FileEntity.builder()
                        .path(bucket)
                        .name(file.getOriginalFilename() == null ? "unknown" : file.getOriginalFilename())
                        .size(file.getSize())
                        .mimetype(file.getContentType() == null ? "application/octet-stream" : file.getContentType())
                        .owner(owner)
                        .build()
        );
        try {
            InputStream inputStream = file.getInputStream();
            s3AsyncClient.putObject(
                    PutObjectRequest
                            .builder()
                            .bucket(bucket)
                            .key(res.getId().toString())
                            .contentType(file.getContentType() == null ? "application/octet-stream" : file.getContentType())
                            .build(),
                    AsyncRequestBody.fromInputStream(inputStream, (long) inputStream.available(), threadPoolTaskExecutor.getThreadPoolExecutor())
            ).exceptionally(e -> {
                log.error("Failed to upload file", e);
                return null;
            });
        } catch (Exception e) {
            log.error("Failed to upload file", e);
        }
        return res;
    }

    public FileEntity uploadFileToDefaultBucket(MultipartFile file, User owner) {
        return uploadFile(externalFileConfig.bucket(), file, owner);
    }

    public void downloadAndStreamToOutput(
            Long id,
            OutputStream outputStream,
            boolean closeStreamAfterTransfer
    ) {
        FileEntity file = fileRepository.findById(id).orElseThrow();
        var download = s3AsyncClient.getObject(
                GetObjectRequest
                        .builder()
                        .bucket(file.getPath())
                        .key(file.getId().toString()).build(),
                AsyncResponseTransformer.toBlockingInputStream()
        );
        // Input stream from s3 will automatically close after transferTo
        try {
            var res = download.join();
            res.transferTo(outputStream);
            // Close the output stream after transferTo
            if (closeStreamAfterTransfer) {
                outputStream.close();
            }
            res.close();
        } catch (Exception e) {
            log.error("Failed to stream file", e);
        }
    }
}
