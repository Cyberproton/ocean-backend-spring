package me.cyberproton.ocean.features.playlist.entity;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import lombok.AllArgsConstructor;
import me.cyberproton.ocean.features.playlist.repository.PlaylistElasticRepository;

@AllArgsConstructor
public class PlaylistListener {
    private final PlaylistElasticRepository playlistElasticRepository;

    @PostPersist
    @PostUpdate
    public void onPlaylistCreatedOrUpdated(PlaylistEntity playlist) {
        playlistElasticRepository.save(playlist);
    }

    @PostRemove
    public void onPlaylistDeleted(PlaylistEntity playlist) {
        playlistElasticRepository.delete(playlist);
    }
}
