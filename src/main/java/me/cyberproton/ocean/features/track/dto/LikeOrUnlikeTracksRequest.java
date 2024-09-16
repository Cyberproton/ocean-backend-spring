package me.cyberproton.ocean.features.track.dto;

import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikeOrUnlikeTracksRequest {
    @Size(min = 1, max = 10)
    private Set<Long> trackIds;
}
