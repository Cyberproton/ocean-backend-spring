package me.cyberproton.ocean.features.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtConfig(
        String secret, Long accessTokenExpirationInMs, Long refreshTokenExpirationInMs) {}
