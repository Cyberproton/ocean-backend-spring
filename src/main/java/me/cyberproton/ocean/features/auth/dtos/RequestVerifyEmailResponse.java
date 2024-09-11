package me.cyberproton.ocean.features.auth.dtos;

import lombok.Builder;

@Builder
public record RequestVerifyEmailResponse(String email) {}
