package me.cyberproton.ocean.features.file;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

@AllArgsConstructor
@Configuration
public class FileConfig {
    private final ExternalFileConfig externalFileConfig;

    @Bean
    public S3Client s3Client() {
        return S3Client
                .builder()
                .region(Region.of(externalFileConfig.region()))
                .endpointOverride(URI.create(externalFileConfig.endpoint()))
                .credentialsProvider(
                        StaticCredentialsProvider.create(AwsBasicCredentials.create(
                                "005c3f5a02aab230000000001",
                                "K005iXlOW2kRoABm/bH1EXs0GnGDGWY"
                        ))
                ).build();
    }
}
