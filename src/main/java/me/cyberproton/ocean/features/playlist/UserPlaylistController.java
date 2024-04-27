package me.cyberproton.ocean.features.playlist;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import me.cyberproton.ocean.annotations.V1ApiRestController;
import me.cyberproton.ocean.config.AppUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@AllArgsConstructor
@V1ApiRestController
@RequestMapping("/users/{userId}/playlists")
public class UserPlaylistController {
    private final PlaylistService playlistService;

    @GetMapping
    public Set<PlaylistResponse> getUserPlaylists(@PathVariable String userId, @AuthenticationPrincipal AppUserDetails userDetails) {
        if (userId.equals("me")) {
            return playlistService.getUserPlaylists(userDetails.getUserId());
        }
        return playlistService.getUserPlaylists(Long.parseLong(userId));
    }

    @PostMapping
    public PlaylistResponse createPlaylist(
            @PathVariable String userId,
            @Valid @RequestBody CreateOrUpdatePlaylist createOrUpdatePlaylist,
            @AuthenticationPrincipal AppUserDetails userDetails
    ) {
        System.out.println("userId: " + userId);
        if (userId.equals("me")) {
            return playlistService.createPlaylist(createOrUpdatePlaylist, userDetails.getUserId());
        }
        System.out.println("adfdsffsdf");
        return playlistService.createPlaylist(createOrUpdatePlaylist, Long.parseLong(userId));
    }
}
