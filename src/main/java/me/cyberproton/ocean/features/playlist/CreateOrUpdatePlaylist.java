package me.cyberproton.ocean.features.playlist;

import lombok.Builder;

import java.util.List;

@Builder
public record CreateOrUpdatePlaylist(
        String name,
        String description,
        boolean isPublic,
        List<Long> trackIds
) {
}
