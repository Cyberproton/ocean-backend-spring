package me.cyberproton.ocean.features.playlist.dto;

import jakarta.validation.constraints.Min;

public record UpdatePlaylistTrackPositionRequest(
        @Min(0) Long startPosition, @Min(1) Long length, @Min(0) Long insertBeforePosition) {}
