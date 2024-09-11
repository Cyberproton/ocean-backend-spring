package me.cyberproton.ocean.config;

public enum AppEnvironment {
    DEVELOPMENT,
    STAGING,
    PRODUCTION;

    public boolean isProduction() {
        return this == PRODUCTION;
    }
}
