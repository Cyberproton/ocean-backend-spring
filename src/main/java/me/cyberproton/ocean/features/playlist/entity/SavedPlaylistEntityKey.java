package me.cyberproton.ocean.features.playlist.entity;

import jakarta.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class SavedPlaylistEntityKey {
    private Long playlistId;

    private Long userId;
}
