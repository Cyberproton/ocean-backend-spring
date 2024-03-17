package me.cyberproton.ocean.auth;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtConfig(String secret, Long expirationInMilliseconds) {
}