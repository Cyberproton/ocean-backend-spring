package me.cyberproton.ocean.features.playlist;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.Set;

@Builder
public record AddPlaylistTrackRequest(
        @Size(min = 1)
        Set<Long> trackIds,
        @Min(0)
        Long position
) {
}
