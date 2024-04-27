package me.cyberproton.ocean.features.playlist;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import me.cyberproton.ocean.annotations.V1ApiRestController;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@V1ApiRestController
@RequestMapping("/playlists")
public class PlaylistController {
    private final PlaylistService playlistService;

    @PutMapping("/{playlistId}")
    public PlaylistResponse updatePlaylist(
            @PathVariable Long playlistId,
            @Valid @RequestBody CreateOrUpdatePlaylist createOrUpdatePlaylist
    ) {
        return playlistService.updatePlaylist(playlistId, createOrUpdatePlaylist);
    }

    @DeleteMapping("/{playlistId}")
    public void deletePlaylist(@PathVariable Long playlistId) {
        playlistService.deletePlaylist(playlistId);
    }
}
