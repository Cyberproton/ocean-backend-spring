package me.cyberproton.ocean.features.track.repository;

import me.cyberproton.ocean.features.track.entity.TrackLikeEntity;
import me.cyberproton.ocean.features.track.entity.TrackLikeEntityKey;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackLikeRepository extends JpaRepository<TrackLikeEntity, TrackLikeEntityKey> {
    Long countAllByTrackId(Long trackId);
}
