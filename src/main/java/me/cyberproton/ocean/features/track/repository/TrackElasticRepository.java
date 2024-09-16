package me.cyberproton.ocean.features.track.repository;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import me.cyberproton.ocean.features.file.FileMapper;
import me.cyberproton.ocean.features.track.dto.TrackLike;
import me.cyberproton.ocean.features.track.entity.TrackDocument;
import me.cyberproton.ocean.features.track.entity.TrackEntity;
import me.cyberproton.ocean.repository.AbstractElasticRepository;

import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TrackElasticRepository extends AbstractElasticRepository<TrackEntity, TrackDocument> {
    private final FileMapper fileMapper;

    public TrackElasticRepository(
            ElasticsearchOperations elasticsearchOperations, FileMapper fileMapper) {
        super(TrackEntity.class, TrackDocument.class, elasticsearchOperations);
        this.fileMapper = fileMapper;
    }

    @Nonnull
    @Override
    protected TrackDocument entityToDocument(TrackEntity track) {
        return TrackDocument.builder()
                .id(track.getId())
                .name(track.getName())
                .trackNumber(track.getTrackNumber())
                .duration(track.getDuration())
                .file(fileMapper.entityToDocument(track.getFile()))
                .albumId(track.getAlbum().getId())
                .numberOfLikes((long) track.getLikedUsers().size())
                .build();
    }

    @Nullable
    @Override
    protected String getEntityId(TrackEntity entity) {
        return entity.getId().toString();
    }

    public void changeNumberOfLikes(List<TrackLike> trackLikes) {
        elasticsearchOperations.bulkUpdate(
                trackLikes.stream()
                        .map(
                                trackLike ->
                                        UpdateQuery.builder(trackLike.trackId().toString())
                                                .withScript(
                                                        "ctx._source.numberOfLikes = "
                                                                + trackLike.numberOfLikes())
                                                .build())
                        .toList(),
                IndexCoordinates.of("track"));
    }
}
