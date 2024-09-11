package me.cyberproton.ocean.features.playlist.controller;

import jakarta.validation.Valid;

import lombok.AllArgsConstructor;

import me.cyberproton.ocean.annotations.V1ApiRestController;
import me.cyberproton.ocean.config.AppUserDetails;
import me.cyberproton.ocean.domain.BaseQuery;
import me.cyberproton.ocean.features.playlist.dto.AddOrRemoveSavedPlaylistsRequest;
import me.cyberproton.ocean.features.playlist.dto.CreateOrUpdatePlaylistRequest;
import me.cyberproton.ocean.features.playlist.dto.PlaylistResponse;
import me.cyberproton.ocean.features.playlist.service.UserPlaylistService;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@AllArgsConstructor
@V1ApiRestController
@RequestMapping("/users/me/playlists")
public class MyPlaylistController {
    private final UserPlaylistService userPlaylistService;

    @GetMapping
    public Set<PlaylistResponse> getPlaylists(
            @AuthenticationPrincipal AppUserDetails userDetails, @Valid BaseQuery query) {
        return userPlaylistService.getUserPlaylists(userDetails.getUserId(), query);
    }

    @PostMapping
    public PlaylistResponse createPlaylist(
            @AuthenticationPrincipal AppUserDetails userDetails,
            @Valid @RequestBody CreateOrUpdatePlaylistRequest createOrUpdatePlaylist) {
        return userPlaylistService.createUserPlaylist(
                userDetails.getUser(), createOrUpdatePlaylist);
    }

    @PostMapping("saved")
    public void savePlaylists(
            @AuthenticationPrincipal AppUserDetails userDetails,
            @Valid @RequestBody AddOrRemoveSavedPlaylistsRequest request) {
        userPlaylistService.savePlaylists(userDetails.getUser(), request.getPlaylistIds());
    }

    @DeleteMapping("saved")
    public void deleteSavedPlaylists(
            @AuthenticationPrincipal AppUserDetails userDetails,
            @Valid @RequestBody AddOrRemoveSavedPlaylistsRequest request) {
        userPlaylistService.deleteSavedPlaylists(userDetails.getUser(), request.getPlaylistIds());
    }
}
