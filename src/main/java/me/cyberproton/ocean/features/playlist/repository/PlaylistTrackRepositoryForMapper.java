package me.cyberproton.ocean.features.playlist.repository;

import me.cyberproton.ocean.features.playlist.entity.PlaylistTrackEntity;
import me.cyberproton.ocean.features.playlist.entity.PlaylistTrackKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaylistTrackRepositoryForMapper
        extends JpaRepository<PlaylistTrackEntity, PlaylistTrackKey> {
    Long countByPlaylistId(Long playlistId);
}
