package me.cyberproton.ocean.features.track.entity;

import jakarta.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class TrackLikeEntityKey implements Serializable {
    private Long trackId;

    private Long userId;
}
