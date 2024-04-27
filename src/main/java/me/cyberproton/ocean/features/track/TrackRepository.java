package me.cyberproton.ocean.features.track;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface TrackRepository extends JpaRepository<Track, Long> {
    Page<Track> findAllByNameContainsIgnoreCase(String name, Pageable pageable);

    Optional<Set<Track>> findAllByPlaylistTracksPlaylistIdOrderByPlaylistTracksTrackPosition(Long playlistId);
}
