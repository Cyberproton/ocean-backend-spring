package me.cyberproton.ocean.features.token;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "token")
public record TokenConfig(PasswordReset passwordReset, EmailVerification emailVerification) {
    public record PasswordReset(long maxAgeInMilliseconds, long intervalInMilliseconds) {}

    public record EmailVerification(long maxAgeInMilliseconds, long intervalInMilliseconds) {}
}
