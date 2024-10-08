package me.cyberproton.ocean.features.file;

import java.net.URI;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.transfer.s3.S3TransferManager;

@AllArgsConstructor
@Configuration
public class FileConfig {
    private final ExternalFileConfig externalFileConfig;

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.of(externalFileConfig.region()))
                .endpointOverride(URI.create(externalFileConfig.endpoint()))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(
                                        externalFileConfig.accessKey(),
                                        externalFileConfig.secretKey())))
                .build();
    }

    @Bean
    public S3AsyncClient s3AsyncClient() {
        return S3AsyncClient.builder()
                .region(Region.of(externalFileConfig.region()))
                .endpointOverride(URI.create(externalFileConfig.endpoint()))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(
                                        externalFileConfig.accessKey(),
                                        externalFileConfig.secretKey())))
                .build();
    }

    @Bean
    public S3TransferManager s3TransferManager(S3AsyncClient s3Client) {
        return S3TransferManager.builder().s3Client(s3Client).build();
    }
}
