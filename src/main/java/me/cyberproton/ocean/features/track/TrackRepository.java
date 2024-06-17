package me.cyberproton.ocean.features.track;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TrackRepository extends JpaRepository<Track, Long> {
    @Query("SELECT t FROM Track t")
    @EntityGraph(attributePaths = {"album", "artists"})
    List<Track> findAllEagerly();

    Page<Track> findAllByNameContainsIgnoreCase(String name, Pageable pageable);

    Optional<Set<Track>> findAllByPlaylistTracksPlaylistIdOrderByPlaylistTracksTrackPosition(Long playlistId);
}
