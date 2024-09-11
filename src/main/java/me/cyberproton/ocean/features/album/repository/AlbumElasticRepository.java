package me.cyberproton.ocean.features.album.repository;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import me.cyberproton.ocean.features.album.entity.AlbumDocument;
import me.cyberproton.ocean.features.album.entity.AlbumEntity;
import me.cyberproton.ocean.features.artist.ArtistEntity;
import me.cyberproton.ocean.features.copyright.CopyrightMapper;
import me.cyberproton.ocean.features.file.FileDocument;
import me.cyberproton.ocean.features.recordlabel.RecordLabelMapper;
import me.cyberproton.ocean.features.user.UserElasticRepository;
import me.cyberproton.ocean.repository.AbstractElasticRepository;

import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Component;

@Component
public class AlbumElasticRepository extends AbstractElasticRepository<AlbumEntity, AlbumDocument> {
    private final CopyrightMapper copyrightMapper;
    private final RecordLabelMapper recordLabelMapper;
    private final UserElasticRepository userElasticRepository;

    public AlbumElasticRepository(
            ElasticsearchOperations elasticsearchOperations,
            CopyrightMapper copyrightMapper,
            RecordLabelMapper recordLabelMapper,
            UserElasticRepository userElasticRepository) {
        super(AlbumEntity.class, AlbumDocument.class, elasticsearchOperations);
        this.copyrightMapper = copyrightMapper;
        this.recordLabelMapper = recordLabelMapper;
        this.userElasticRepository = userElasticRepository;
    }

    @Nonnull
    @Override
    protected AlbumDocument entityToDocument(AlbumEntity album) {
        return AlbumDocument.builder()
                .id(album.getId())
                .type(album.getType())
                .name(album.getName())
                .description(album.getDescription())
                .releaseDate(album.getReleaseDate())
                .covers(
                        album.getCovers().stream()
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
                .recordLabel(recordLabelMapper.entityToDocument(album.getRecordLabel()))
                .copyrights(
                        album.getCopyrights().stream()
                                .map(this.copyrightMapper::entityToDocument)
                                .toList())
                .artists(
                        album.getTracks().stream()
                                .flatMap(track -> track.getArtists().stream())
                                .map(ArtistEntity::getUser)
                                .map(userElasticRepository::entityToDocument)
                                .toList())
                .build();
    }

    @Nullable @Override
    protected String getEntityId(AlbumEntity entity) {
        return entity.getId().toString();
    }
}
