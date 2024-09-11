package me.cyberproton.ocean.features.playlist.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Builder
@Data
public class AddOrRemoveSavedPlaylistsRequest {
    private Set<Long> playlistIds;
}
