package me.cyberproton.ocean.features.track.controller;

import jakarta.validation.Valid;

import lombok.AllArgsConstructor;

import me.cyberproton.ocean.annotations.V1ApiRestController;
import me.cyberproton.ocean.domain.BaseQuery;
import me.cyberproton.ocean.features.playlist.dto.AddPlaylistTrackRequest;
import me.cyberproton.ocean.features.playlist.dto.RemovePlaylistTrackRequest;
import me.cyberproton.ocean.features.playlist.dto.UpdatePlaylistTrackPositionRequest;
import me.cyberproton.ocean.features.track.dto.TrackResponse;
import me.cyberproton.ocean.features.track.service.PlaylistTrackService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@V1ApiRestController
@RequestMapping("/playlists/{playlistId}/tracks")
public class PlaylistTrackController {
    private final PlaylistTrackService playlistTrackService;

    @GetMapping
    public List<TrackResponse> getPlaylistTracks(
            @PathVariable Long playlistId, @Valid BaseQuery query) {
        return playlistTrackService.getPlaylistTracks(playlistId, query);
    }

    @PostMapping
    public void addTrackToPlaylist(
            @PathVariable Long playlistId, @Valid @RequestBody AddPlaylistTrackRequest request) {
        playlistTrackService.addTracksToPlaylist(playlistId, request);
    }

    @DeleteMapping
    public void removeTrackFromPlaylist(
            @PathVariable Long playlistId, @Valid @RequestBody RemovePlaylistTrackRequest request) {
        playlistTrackService.removeTracksFromPlaylist(playlistId, request.trackIds());
    }

    @PutMapping
    public void updatePlaylistTrack(
            @PathVariable Long playlistId,
            @Valid @RequestBody UpdatePlaylistTrackPositionRequest request) {
        playlistTrackService.updatePlaylistTracksPosition(playlistId, request);
    }
}
