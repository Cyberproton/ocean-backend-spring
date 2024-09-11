package me.cyberproton.ocean.seed;

import jakarta.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import me.cyberproton.ocean.features.file.FileEntity;
import me.cyberproton.ocean.features.file.FileService;
import me.cyberproton.ocean.features.user.UserEntity;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
@Profile("seeder")
public class ImageUploaderDownloader {
    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;
    private final FileService fileService;

    public String download(String imageUrl, @Nullable String folderPath) {
        try (InputStream in = new URL(imageUrl).openStream()) {
            File tempFolder = getTempFolder(folderPath);
            tempFolder.mkdirs();
            String imageName = UUID.randomUUID().toString();
            File imageFile = new File(tempFolder, imageName + ".jpg");
            Files.copy(in, imageFile.toPath());
            return imageFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @SneakyThrows
    public List<String> downloadAll(
            List<String> imageUrls, @Nullable String folderPath, boolean checkExistingImages) {
        List<CompletableFuture<String>> futures = new ArrayList<>();
        File tempFolder = getTempFolder(folderPath);
        tempFolder.mkdirs();
        int requiredFileCount = imageUrls.size();
        int existingFileCount = tempFolder.list().length;
        int remainingFileCount = requiredFileCount - existingFileCount;
        for (int i = 0; i < Math.min(existingFileCount, requiredFileCount); i++) {
            String existingImagePath = tempFolder.list()[i];
            futures.add(
                    CompletableFuture.completedFuture(
                            tempFolder.getAbsolutePath() + File.separator + existingImagePath));
        }
        for (int i = 0; i < remainingFileCount; i++) {
            String imageUrl = imageUrls.get(i);
            CompletableFuture<String> future =
                    CompletableFuture.supplyAsync(
                            () -> download(imageUrl, folderPath),
                            threadPoolTaskExecutor.getThreadPoolExecutor());
            futures.add(future);
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        return futures.stream().map(CompletableFuture::join).toList();
    }

    public List<FileEntity> downloadAndUploadAll(
            List<String> imageUrls,
            List<UserEntity> owners,
            @Nullable String folderPath,
            boolean checkExistingImages,
            @Nullable Consumer<FileEntity> fileEntityModifier) {
        List<String> imagePaths = downloadAll(imageUrls, folderPath, checkExistingImages);
        List<FileEntity> files = new ArrayList<>();
        for (int i = 0; i < imagePaths.size(); i++) {
            File file = new File(imagePaths.get(i));
            UserEntity owner = owners.get(i);
            FileEntity entity =
                    fileService.uploadFileToDefaultBucket(file, owner, fileEntityModifier);
            files.add(entity);
        }
        return files;
    }

    public List<FileEntity> downloadAndUploadAll(
            List<String> imageUrls,
            List<UserEntity> owners,
            @Nullable String folderPath,
            boolean checkExistingImages) {
        return downloadAndUploadAll(imageUrls, owners, folderPath, checkExistingImages, null);
    }

    private File getTempFolder(String folderPath) {
        return new File("tmp" + (folderPath == null ? "" : File.separator + folderPath));
    }
}
