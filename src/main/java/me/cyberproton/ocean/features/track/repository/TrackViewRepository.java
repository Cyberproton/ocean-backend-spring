package me.cyberproton.ocean.features.track.repository;

import me.cyberproton.ocean.features.track.view.TrackView;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackViewRepository
        extends JpaRepository<TrackView, Long>, TrackViewRepositoryExtension {}
