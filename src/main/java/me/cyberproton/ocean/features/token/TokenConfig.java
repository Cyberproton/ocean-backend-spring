package me.cyberproton.ocean.features.token;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "token")
public record TokenConfig(ResetPassword resetPassword) {
    public record ResetPassword(long maxAgeInMilliseconds, long intervalInMilliseconds) {
    }
}
