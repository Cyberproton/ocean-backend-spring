package me.cyberproton.ocean.features.playlist.repository;


import me.cyberproton.ocean.features.playlist.view.PlaylistView;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaylistViewRepository extends JpaRepository<PlaylistView, Long> {
    Page<PlaylistView> findAllByOwnerId(Long ownerId, Pageable pageable);
}
