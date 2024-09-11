package me.cyberproton.ocean.features.playlist.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import java.util.Set;
import lombok.Builder;

@Builder
public record AddPlaylistTrackRequest(@Size(min = 1) Set<Long> trackIds, @Min(0) Long position) {}
