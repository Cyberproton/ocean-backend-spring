package me.cyberproton.ocean.features.auth.dtos;

import lombok.Builder;

@Builder
public record ConfirmVerifyEmailRequest(String token) {}
