package me.cyberproton.ocean.features.playlist.dto;

import me.cyberproton.ocean.features.file.ImageResponse;
import me.cyberproton.ocean.features.profile.dto.ProfileResponse;

import java.util.List;

public record PlaylistResponse(
        Long id,
        String name,
        String description,
        List<ImageResponse> covers,
        ProfileResponse owner,
        Long totalTracks,
        Long popularity) {}
