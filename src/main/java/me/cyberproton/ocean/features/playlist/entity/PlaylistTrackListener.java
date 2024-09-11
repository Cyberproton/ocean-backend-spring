package me.cyberproton.ocean.features.playlist.entity;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import lombok.AllArgsConstructor;
import me.cyberproton.ocean.features.playlist.repository.PlaylistElasticRepository;

@AllArgsConstructor
public class PlaylistTrackListener {
    private final PlaylistElasticRepository playlistElasticRepository;

    @PostPersist
    @PostUpdate
    public void onPersistAndUpdate(PlaylistTrackEntity entity) {
        entity.getPlaylist().getPlaylistTracks().add(entity);
        playlistElasticRepository.save(entity.getPlaylist());
    }

    @PostRemove
    public void onDelete(PlaylistTrackEntity entity) {
        entity.getPlaylist()
                .getPlaylistTracks()
                .removeIf(playlistTrack -> playlistTrack.getId().equals(entity.getId()));
        playlistElasticRepository.delete(entity.getPlaylist());
    }
}
