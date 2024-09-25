package me.cyberproton.ocean.features.playlist.controller;

import jakarta.validation.Valid;

import lombok.AllArgsConstructor;

import me.cyberproton.ocean.annotations.V1ApiRestController;
import me.cyberproton.ocean.config.ExternalAppConfig;
import me.cyberproton.ocean.domain.BaseQuery;
import me.cyberproton.ocean.domain.PaginationResponse;
import me.cyberproton.ocean.features.playlist.dto.CreateOrUpdatePlaylistRequest;
import me.cyberproton.ocean.features.playlist.dto.PlaylistResponse;
import me.cyberproton.ocean.features.playlist.service.PlaylistService;
import me.cyberproton.ocean.features.playlist.service.UserPlaylistService;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@AllArgsConstructor
@V1ApiRestController
@RequestMapping("/playlists")
public class PlaylistController {
    private final PlaylistService playlistService;
    private final UserPlaylistService userPlaylistService;
    private final ExternalAppConfig externalAppConfig;

    @PutMapping("/{playlistId}")
    public PlaylistResponse updatePlaylist(
            @PathVariable Long playlistId,
            @Valid @RequestBody CreateOrUpdatePlaylistRequest createOrUpdatePlaylist) {
        return playlistService.updatePlaylist(playlistId, createOrUpdatePlaylist);
    }

    @DeleteMapping("/{playlistId}")
    public void deletePlaylist(@PathVariable Long playlistId) {
        playlistService.deletePlaylist(playlistId);
    }

    @GetMapping("top")
    public PaginationResponse<PlaylistResponse> getTopPlaylist(BaseQuery query) {
        return PaginationResponse.fromPage(
                playlistService.getTopPlaylists(query),
                UriComponentsBuilder.fromHttpUrl(externalAppConfig.domain())
                        .pathSegment(externalAppConfig.apiV1Path())
                        .pathSegment("playlists")
                        .pathSegment("top")
                        .toUriString());
    }
}
