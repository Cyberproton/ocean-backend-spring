package me.cyberproton.ocean.features.album.entity;

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
public class SavedAlbumEntityKey {
    private Long albumId;

    private Long userId;
}
