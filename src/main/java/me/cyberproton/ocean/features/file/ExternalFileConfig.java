package me.cyberproton.ocean.features.file;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "file")
public record ExternalFileConfig(
        String endpoint, String bucket, String region, String accessKey, String secretKey) {}
