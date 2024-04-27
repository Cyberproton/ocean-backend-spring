package me.cyberproton.ocean.features.playlist;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import me.cyberproton.ocean.annotations.V1ApiRestController;
import me.cyberproton.ocean.features.track.TrackResponse;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@AllArgsConstructor
@V1ApiRestController
@RequestMapping("/playlists/{playlistId}/tracks")
public class PlaylistTrackController {
    private final PlaylistService playlistService;

    @PostMapping
    public Set<TrackResponse> addTrackToPlaylist(
            @PathVariable Long playlistId,
            @Valid @RequestBody AddPlaylistTrackRequest request
    ) {
        return playlistService.addTracksToPlaylist(playlistId, request);
    }

    @PutMapping
    public Set<TrackResponse> updatePlaylistTrack(
            @PathVariable Long playlistId,
            @Valid @RequestBody UpdatePlaylistTrackPositionRequest request
    ) {
        return playlistService.updatePlaylistTracksPosition(playlistId, request);
    }
}
