package me.cyberproton.ocean.features.album.dto;

import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AddOrRemoveSavedAlbumsRequest {
    @Size(min = 1, max = 20)
    private Set<Long> albumIds;
}
