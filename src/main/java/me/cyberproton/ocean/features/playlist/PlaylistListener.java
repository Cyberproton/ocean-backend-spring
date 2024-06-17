package me.cyberproton.ocean.features.playlist;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import lombok.AllArgsConstructor;
import me.cyberproton.ocean.features.file.FileDocument;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

@AllArgsConstructor
public class PlaylistListener {
    private final ElasticsearchOperations elasticsearchOperations;

    @PostPersist
    @PostUpdate
    public void onPlaylistCreatedOrUpdated(Playlist playlist) {
        PlaylistDocument playlistDocument =
                PlaylistDocument
                        .builder()
                        .id(playlist.getId())
                        .name(playlist.getName())
                        .description(playlist.getDescription())
                        .isPublic(playlist.isPublic())
                        .covers(playlist.getCovers()
                                        .stream()
                                        .map(fileEntity ->
                                                     FileDocument.builder()
                                                                 .id(fileEntity.getId())
                                                                 .type(fileEntity.getType())
                                                                 .name(fileEntity.getName())
                                                                 .mimetype(
                                                                         fileEntity.getMimetype())
                                                                 .path(fileEntity.getPath())
                                                                 .size(fileEntity.getSize())
                                                                 .isPublic(
                                                                         fileEntity.isPublic())
                                                                 .width(fileEntity.getWidth())
                                                                 .height(fileEntity.getHeight())
                                                                 .build())
                                        .toList())
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
