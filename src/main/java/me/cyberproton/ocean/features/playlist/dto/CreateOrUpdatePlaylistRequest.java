package me.cyberproton.ocean.features.playlist.dto;

import jakarta.validation.constraints.Size;

import lombok.Builder;

import me.cyberproton.ocean.features.playlist.util.PlaylistConstants;

import java.util.List;

@Builder
public record CreateOrUpdatePlaylistRequest(
        String name,
        @Size(max = PlaylistConstants.PLAYLIST_DESCRIPTION_MAX_LENGTH) String description,
        boolean isPublic,
        List<Long> trackIds) {}
