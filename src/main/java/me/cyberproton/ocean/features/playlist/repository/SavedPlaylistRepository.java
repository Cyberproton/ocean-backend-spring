package me.cyberproton.ocean.features.playlist.repository;

import me.cyberproton.ocean.features.playlist.entity.SavedPlaylistEntity;
import me.cyberproton.ocean.features.playlist.entity.SavedPlaylistEntityKey;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SavedPlaylistRepository
        extends JpaRepository<SavedPlaylistEntity, SavedPlaylistEntityKey> {}
