package me.cyberproton.ocean.features.album.repository;

import me.cyberproton.ocean.features.album.entity.SavedAlbumEntity;
import me.cyberproton.ocean.features.album.entity.SavedAlbumEntityKey;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SavedAlbumRepository
        extends JpaRepository<SavedAlbumEntity, SavedAlbumEntityKey> {}
