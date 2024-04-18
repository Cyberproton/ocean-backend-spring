package me.cyberproton.ocean.features.playlist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    Optional<Set<Playlist>> findByOwnerId(Long userId);
}
