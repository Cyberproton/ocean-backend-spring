package me.cyberproton.ocean.features.playlist;

import lombok.Builder;

import java.util.Set;

@Builder
public record RemovePlaylistTrackRequest(
        Set<Long> trackIds
) {
}
