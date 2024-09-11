package me.cyberproton.ocean.features.token;

import lombok.Builder;

@Builder
public record TokenResult(Token token, String rawToken) {}
