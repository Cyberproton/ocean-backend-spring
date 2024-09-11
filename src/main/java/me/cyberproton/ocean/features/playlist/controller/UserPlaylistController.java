package me.cyberproton.ocean.features.playlist.controller;

import jakarta.validation.Valid;

import lombok.AllArgsConstructor;

import me.cyberproton.ocean.annotations.V1ApiRestController;
import me.cyberproton.ocean.config.AppUserDetails;
import me.cyberproton.ocean.domain.BaseQuery;
import me.cyberproton.ocean.features.playlist.dto.PlaylistResponse;
import me.cyberproton.ocean.features.playlist.service.PlaylistService;
import me.cyberproton.ocean.features.playlist.service.UserPlaylistService;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@AllArgsConstructor
@V1ApiRestController
@RequestMapping("/users/{userId}/playlists")
public class UserPlaylistController {
    private final PlaylistService playlistService;
    private final UserPlaylistService userPlaylistService;

    @GetMapping
    public Set<PlaylistResponse> getUserPlaylists(
            @PathVariable String userId,
            @AuthenticationPrincipal AppUserDetails userDetails,
            @Valid BaseQuery query) {
        if (userId.equals("me")) {
            return userPlaylistService.getUserPlaylists(userDetails.getUserId(), query);
        }
        return userPlaylistService.getUserPlaylists(Long.parseLong(userId), query);
    }
}
