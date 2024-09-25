package me.cyberproton.ocean.features.track.dto;

import jakarta.annotation.Nullable;

public record TrackPlayDto(
        Long trackId, @Nullable Long albumId, Long numberOfPlays, Long userPlayedAtTimestamp) {}
