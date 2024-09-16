package me.cyberproton.ocean.features.track.repository;

import me.cyberproton.ocean.features.track.view.TrackWithLikeCountView;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackWithLikeCountViewRepository
        extends JpaRepository<TrackWithLikeCountView, Long> {}
