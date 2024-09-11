package me.cyberproton.ocean.seed;

import java.util.List;
import lombok.AllArgsConstructor;
import me.cyberproton.ocean.features.file.ExternalFileConfig;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

@Profile("seeder")
@Component
@AllArgsConstructor
public class StorageResetter {
    private final S3Client s3Client;
    private final ExternalFileConfig fileConfig;

    public void reset() {
        ListObjectsV2Response response =
                s3Client.listObjectsV2(
                        ListObjectsV2Request.builder().bucket(fileConfig.bucket()).build());
        List<S3Object> contents = response.contents();
        // Delete all objects in the bucket
        // 1000 objects per request
        while (!contents.isEmpty()) {
            DeleteObjectsResponse deleteObjectsResponse =
                    s3Client.deleteObjects(
                            DeleteObjectsRequest.builder()
                                    .bucket(fileConfig.bucket())
                                    .delete(
                                            Delete.builder()
                                                    .objects(
                                                            contents.stream()
                                                                    .map(
                                                                            content ->
                                                                                    ObjectIdentifier
                                                                                            .builder()
                                                                                            .key(
                                                                                                    content
                                                                                                            .key())
                                                                                            .build())
                                                                    .toList()
                                                                    .subList(
                                                                            0,
                                                                            Math.min(
                                                                                    contents.size(),
                                                                                    1000)))
                                                    .build())
                                    .build());

            contents =
                    s3Client.listObjectsV2(
                                    ListObjectsV2Request.builder()
                                            .bucket(fileConfig.bucket())
                                            .build())
                            .contents();
        }
    }
}
