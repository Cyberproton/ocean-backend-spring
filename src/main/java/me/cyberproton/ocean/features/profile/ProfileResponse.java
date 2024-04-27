package me.cyberproton.ocean.features.profile;

import lombok.Builder;

@Builder
public record ProfileResponse(
        String name,
        String bio,
        String avatarUrl,
        String bannerUrl
) {
}
