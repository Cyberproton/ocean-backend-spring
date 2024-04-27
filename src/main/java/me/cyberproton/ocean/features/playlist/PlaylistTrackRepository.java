package me.cyberproton.ocean.features.playlist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaylistTrackRepository extends JpaRepository<PlaylistTrack, PlaylistTrackKey> {
    Optional<List<PlaylistTrack>> findAllByPlaylistIdOrderByTrackPosition(Long playlistId);

}
