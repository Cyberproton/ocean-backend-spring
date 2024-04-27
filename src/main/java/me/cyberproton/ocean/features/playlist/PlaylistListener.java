package me.cyberproton.ocean.features.playlist;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import lombok.AllArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

@AllArgsConstructor
public class PlaylistListener {
    private final ElasticsearchOperations elasticsearchOperations;

    @PostPersist
    @PostUpdate
    public void onPlaylistCreatedOrUpdated(Playlist playlist) {
        PlaylistDocument playlistDocument = PlaylistDocument.builder()
                .id(playlist.getId())
                .name(playlist.getName())
                .description(playlist.getDescription())
                .isPublic(playlist.isPublic())
                .coverId(playlist.getCover().getId())
                .ownerId(playlist.getOwner().getId())
                .build();
        elasticsearchOperations.save(playlistDocument);
    }

    @PostRemove
    public void onPlaylistDeleted(Playlist playlist) {
        elasticsearchOperations.delete(
                playlist.getId().toString(),
                PlaylistDocument.class
        );
    }
}
