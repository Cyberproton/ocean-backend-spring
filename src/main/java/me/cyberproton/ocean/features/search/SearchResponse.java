package me.cyberproton.ocean.features.search;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;

import me.cyberproton.ocean.domain.PaginationResponse;
import me.cyberproton.ocean.features.album.dto.AlbumResponse;
import me.cyberproton.ocean.features.playlist.dto.PlaylistResponse;
import me.cyberproton.ocean.features.profile.dto.ProfileResponse;
import me.cyberproton.ocean.features.track.dto.TrackResponse;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record SearchResponse(
        PaginationResponse<TrackResponse> tracks,
        PaginationResponse<AlbumResponse> albums,
        PaginationResponse<ProfileResponse> artists,
        PaginationResponse<PlaylistResponse> playlists) {}
