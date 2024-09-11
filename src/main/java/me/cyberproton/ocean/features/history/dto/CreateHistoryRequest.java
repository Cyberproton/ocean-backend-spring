package me.cyberproton.ocean.features.history.dto;

import jakarta.validation.constraints.NotNull;

import me.cyberproton.ocean.features.history.entity.HistoryType;

// Validate fields first, then the class
// @GroupSequence({CreateHistoryRequest.class, ClassValidationGroup.class})
// @IsValidHistory(groups = ClassValidationGroup.class)
public record CreateHistoryRequest(
        @NotNull HistoryType type, Long trackId, Long albumId, Long artistId, Long playlistId) {}
