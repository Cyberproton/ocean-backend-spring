package me.cyberproton.ocean.features.playlist;

import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;

@Builder
public record CreateOrUpdatePlaylist(
        String name,
        @Size(max = PlaylistConstants.PLAYLIST_DESCRIPTION_MAX_LENGTH)
        String description,
        boolean isPublic,
        List<Long> trackIds
) {
}
