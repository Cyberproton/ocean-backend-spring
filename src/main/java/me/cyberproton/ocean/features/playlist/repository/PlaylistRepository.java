package me.cyberproton.ocean.features.playlist.repository;

import java.util.Optional;
import java.util.Set;

import me.cyberproton.ocean.features.playlist.entity.PlaylistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaylistRepository extends JpaRepository<PlaylistEntity, Long> {
    Optional<Set<PlaylistEntity>> findByOwnerId(Long userId);
}
