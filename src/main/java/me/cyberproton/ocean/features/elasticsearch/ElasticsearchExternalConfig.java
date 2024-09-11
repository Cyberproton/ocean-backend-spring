package me.cyberproton.ocean.features.elasticsearch;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.elasticsearch")
public record ElasticsearchExternalConfig(String uris, String username, String password) {}
