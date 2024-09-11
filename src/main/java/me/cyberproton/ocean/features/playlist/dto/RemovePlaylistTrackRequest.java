package me.cyberproton.ocean.features.playlist.dto;

import java.util.Set;
import lombok.Builder;

@Builder
public record RemovePlaylistTrackRequest(Set<Long> trackIds) {}
