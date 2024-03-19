package me.cyberproton.ocean.features.auth;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "auth")
public record ExternalAuthConfig(boolean disableCsrf) {
}
