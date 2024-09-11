package me.cyberproton.ocean.features.track.repository;


import me.cyberproton.ocean.features.track.dto.TrackView;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackViewRepository
        extends JpaRepository<TrackView, Long>, TrackViewRepositoryExtension {}
