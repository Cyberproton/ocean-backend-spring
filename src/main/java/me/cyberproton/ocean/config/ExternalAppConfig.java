package me.cyberproton.ocean.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public record ExternalAppConfig(AppEnvironment env, String domain, String apiV1Path) {}
