package me.cyberproton.ocean.features.track;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record CreateOrUpdateTrackRequest(
        @NotBlank
        @Size(max = TrackConstants.TRACK_NAME_MAX_LENGTH)
        String name,
        @Min(1)
        Integer trackNumber,
        @Min(1)
        Long duration,
        Long albumId,
        @NotEmpty
        Set<Long> artistIds
) {
}
