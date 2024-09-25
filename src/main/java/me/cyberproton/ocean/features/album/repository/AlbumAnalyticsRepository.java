package me.cyberproton.ocean.features.album.repository;

import me.cyberproton.ocean.features.album.entity.AlbumAnalyticsEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlbumAnalyticsRepository extends JpaRepository<AlbumAnalyticsEntity, Long> {
    List<AlbumAnalyticsEntity> findAllByAlbumTracksIdIn(List<Long> trackIds);
}
