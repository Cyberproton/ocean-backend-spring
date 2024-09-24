package me.cyberproton.ocean.features.track.dto;

import lombok.Builder;

import me.cyberproton.ocean.features.album.dto.AlbumResponse;
import me.cyberproton.ocean.features.profile.dto.ProfileResponse;

import java.util.List;

@Builder
public record TrackResponse(
        Long id,
        String name,
        Integer trackNumber,
        Integer duration,
        Long numberOfPlays,
        Long popularity,
        AlbumResponse album,
        List<ProfileResponse> artists) {}
