package me.cyberproton.ocean.features.playlist.repository;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import me.cyberproton.ocean.features.file.FileDocument;
import me.cyberproton.ocean.features.playlist.entity.PlaylistDocument;
import me.cyberproton.ocean.features.playlist.entity.PlaylistEntity;
import me.cyberproton.ocean.repository.AbstractElasticRepository;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Component;

@Component
public class PlaylistElasticRepository
        extends AbstractElasticRepository<PlaylistEntity, PlaylistDocument> {
    public PlaylistElasticRepository(ElasticsearchOperations elasticsearchOperations) {
        super(PlaylistEntity.class, PlaylistDocument.class, elasticsearchOperations);
    }

    @Nonnull
    @Override
    protected PlaylistDocument entityToDocument(PlaylistEntity playlist) {
        return PlaylistDocument.builder()
                .id(playlist.getId())
                .name(playlist.getName())
                .description(playlist.getDescription())
                .isPublic(playlist.isPublic())
                .covers(
                        playlist.getCovers() == null
                                ? null
                                : playlist.getCovers().stream()
                                        .map(
                                                fileEntity ->
                                                        FileDocument.builder()
                                                                .id(fileEntity.getId())
                                                                .type(fileEntity.getType())
                                                                .name(fileEntity.getName())
                                                                .mimetype(fileEntity.getMimetype())
                                                                .path(fileEntity.getPath())
                                                                .size(fileEntity.getSize())
                                                                .isPublic(fileEntity.isPublic())
                                                                .width(fileEntity.getWidth())
                                                                .height(fileEntity.getHeight())
                                                                .build())
                                        .toList())
                .ownerId(playlist.getOwner().getId())
                .totalTracks(
                        playlist.getPlaylistTracks() != null
                                ? playlist.getPlaylistTracks().size()
                                : 0)
                .build();
    }

    @Nullable @Override
    protected String getEntityId(PlaylistEntity entity) {
        return entity.getId().toString();
    }
}
