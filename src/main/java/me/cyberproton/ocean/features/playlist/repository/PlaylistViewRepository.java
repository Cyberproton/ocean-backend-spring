package me.cyberproton.ocean.features.playlist.repository;

import com.blazebit.persistence.spring.data.repository.EntityViewRepository;

import me.cyberproton.ocean.features.playlist.dto.PlaylistView;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PlaylistViewRepository extends EntityViewRepository<PlaylistView, Long> {
    Page<PlaylistView> findAllByOwnerId(Long ownerId, Pageable pageable);
}
