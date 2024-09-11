package me.cyberproton.ocean.features.playlist.repository;

import java.util.List;
import java.util.Optional;

import me.cyberproton.ocean.features.playlist.entity.PlaylistTrackEntity;
import me.cyberproton.ocean.features.playlist.entity.PlaylistTrackKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaylistTrackRepository
        extends JpaRepository<PlaylistTrackEntity, PlaylistTrackKey> {
    Optional<List<PlaylistTrackEntity>> findAllByPlaylistIdOrderByTrackPosition(Long playlistId);
}
