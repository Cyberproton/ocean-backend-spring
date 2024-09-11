package me.cyberproton.ocean.features.auth.dtos;

import lombok.Builder;
import me.cyberproton.ocean.features.user.UserResponse;

@Builder
public record RefreshTokenResponse(String accessToken, String refreshToken, UserResponse user) {}
