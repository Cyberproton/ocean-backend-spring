package me.cyberproton.ocean.features.playlist.dto;

import jakarta.validation.constraints.Size;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Builder
@Data
public class AddOrRemoveSavedPlaylistsRequest {
    @Size(min = 1, max = 20)
    private Set<Long> playlistIds;
}
