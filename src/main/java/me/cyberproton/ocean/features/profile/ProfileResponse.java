package me.cyberproton.ocean.features.profile;

import lombok.Builder;

@Builder
public record ProfileResponse(
        Long id,
        String username,
        String name,
        String bio,
        String avatarUrl,
        String bannerUrl,
        Long numberOfFollowers,
        Long numberOfFollowings
) {
}
