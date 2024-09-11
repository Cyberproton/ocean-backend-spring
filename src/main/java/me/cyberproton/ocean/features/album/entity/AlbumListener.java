package me.cyberproton.ocean.features.album.entity;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;

import lombok.AllArgsConstructor;

import me.cyberproton.ocean.features.album.repository.AlbumElasticRepository;

@AllArgsConstructor
public class AlbumListener {
    private final AlbumElasticRepository albumElasticRepository;

    @PostPersist
    @PostUpdate
    public void onCreatedOrUpdated(AlbumEntity album) {
        albumElasticRepository.save(album);
    }

    @PostRemove
    public void onDeleted(AlbumEntity album) {
        albumElasticRepository.delete(album);
    }
}
