package me.cyberproton.ocean.features.playlist.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import me.cyberproton.ocean.annotations.V1ApiRestController;
import me.cyberproton.ocean.config.AppUserDetails;
import me.cyberproton.ocean.features.playlist.dto.PlaylistResponse;
import me.cyberproton.ocean.features.playlist.service.PlaylistService;
import me.cyberproton.ocean.features.playlist.service.UserPlaylistService;
import me.cyberproton.ocean.features.playlist.dto.CreateOrUpdatePlaylistRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@V1ApiRestController
@RequestMapping("/playlists")
public class PlaylistController {
    private final PlaylistService playlistService;
    private final UserPlaylistService userPlaylistService;

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
}
