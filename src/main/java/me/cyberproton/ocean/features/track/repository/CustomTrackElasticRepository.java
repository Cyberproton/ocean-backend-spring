package me.cyberproton.ocean.features.track.repository;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import me.cyberproton.ocean.features.file.FileMapper;
import me.cyberproton.ocean.features.track.dto.TrackLikeDto;
import me.cyberproton.ocean.features.track.dto.TrackPlayDto;
import me.cyberproton.ocean.features.track.entity.TrackDocument;
import me.cyberproton.ocean.features.track.entity.TrackEntity;
import me.cyberproton.ocean.repository.AbstractElasticRepository;

import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.MultiGetItem;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomTrackElasticRepository
        extends AbstractElasticRepository<TrackEntity, TrackDocument> {
    private final FileMapper fileMapper;

    public CustomTrackElasticRepository(
            ElasticsearchOperations elasticsearchOperations, FileMapper fileMapper) {
        super(TrackEntity.class, TrackDocument.class, elasticsearchOperations);
        this.fileMapper = fileMapper;
    }

    /**
     * Calculate popularity based on the number of plays, likes, and timestamps of likes and plays.
     *
     * @param trackDocument Track document
     * @return Popularity
     */
    private static long calculatePopularity(TrackDocument trackDocument) {
        long numberOfPlays =
                trackDocument.getNumberOfPlays() == null ? 0 : trackDocument.getNumberOfPlays();
        long numberOfLikes =
                trackDocument.getNumberOfLikes() == null ? 0 : trackDocument.getNumberOfLikes();
        long likeTotalTimestampInMinutes =
                trackDocument.getLikeTotalTimestampInMinutes() == null
                        ? 0
                        : trackDocument.getLikeTotalTimestampInMinutes();
        long playTotalTimestampInMinutes =
                trackDocument.getPlayTotalTimestampInMinutes() == null
                        ? 0
                        : trackDocument.getPlayTotalTimestampInMinutes();
        return numberOfPlays
                + numberOfLikes * 5
                + likeTotalTimestampInMinutes / 1440
                + playTotalTimestampInMinutes / 1440;
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

    public void changeNumberOfLikes(List<TrackLikeDto> trackLikeDtos) {
        List<String> ids = trackLikeDtos.stream().map(t -> t.trackId().toString()).toList();
        System.out.println("adsfdsfd " + ids);
        CriteriaQuery query = new CriteriaQuery(new Criteria());
        query.setIds(ids);
        List<TrackDocument> items =
                elasticsearchOperations
                        .multiGet(query, TrackDocument.class, IndexCoordinates.of("track"))
                        .stream()
                        .map(MultiGetItem::getItem)
                        .toList();
        items.forEach(
                trackDocument -> {
                    TrackLikeDto trackLikeDto =
                            trackLikeDtos.stream()
                                    .filter(dto -> dto.trackId().equals(trackDocument.getId()))
                                    .findFirst()
                                    .orElseThrow();
                    trackDocument.setNumberOfLikes(trackLikeDto.numberOfLikes());
                    long likeTotalTimestampInMinutes =
                            trackDocument.getLikeTotalTimestampInMinutes() == null
                                    ? 0
                                    : trackDocument.getLikeTotalTimestampInMinutes();
                    trackDocument.setLikeTotalTimestampInMinutes(
                            likeTotalTimestampInMinutes
                                    + (trackLikeDto.userLikedAtTimestamp() / 60000)
                                            * (trackLikeDto.type() == TrackLikeDto.LikeType.LIKE
                                                    ? 1
                                                    : -1));
                    trackDocument.setPopularity(calculatePopularity(trackDocument));
                });
        items.forEach(
                trackDocument ->
                        System.out.println(
                                trackDocument.getId() + " " + trackDocument.getPopularity()));
        elasticsearchOperations.save(items);
        //        elasticsearchOperations.bulkUpdate(
        //                trackLikeDtos.stream()
        //                        .map(
        //                                trackLikeDto -> {
        //                                    long userLikedAtInMinutes =
        //                                            trackLikeDto.userLikedAtTimestamp() / 60000;
        //                                    return
        // UpdateQuery.builder(trackLikeDto.trackId().toString())
        //                                            .withScript(
        //                                                    "ctx._source.numberOfLikes = "
        //                                                            + trackLikeDto.numberOfLikes()
        //                                                            + ";"
        //                                                            + "if
        // (ctx._source.likeTotalTimestampInMinutes == null) { "
        //                                                            +
        // "ctx._source.likeTotalTimestampInMinutes = "
        //                                                            + userLikedAtInMinutes
        //                                                            + "} else { "
        //                                                            +
        // "ctx._source.likeTotalTimestampInMinutes += "
        //                                                            + (trackLikeDto.type()
        //                                                                            ==
        // TrackLikeDto.LikeType
        //                                                                                    .LIKE
        //                                                                    ? userLikedAtInMinutes
        //                                                                    :
        // -userLikedAtInMinutes)
        //                                                            + "}")
        //                                            .build();
        //                                })
        //                        .toList(),
        //                IndexCoordinates.of("track"));
    }

    public void changeNumberOfPlays(List<TrackPlayDto> trackPlayDtos) {
        List<TrackDocument> items =
                elasticsearchOperations
                        .multiGet(
                                new CriteriaQuery(
                                        Criteria.where("id")
                                                .in(
                                                        trackPlayDtos.stream()
                                                                .map(TrackPlayDto::trackId)
                                                                .toList())),
                                TrackDocument.class,
                                IndexCoordinates.of("track"))
                        .stream()
                        .map(MultiGetItem::getItem)
                        .toList();
        items.forEach(
                trackDocument -> {
                    TrackPlayDto trackPlayDto =
                            trackPlayDtos.stream()
                                    .filter(dto -> dto.trackId().equals(trackDocument.getId()))
                                    .findFirst()
                                    .orElseThrow();
                    trackDocument.setNumberOfPlays(trackPlayDto.numberOfPlays());
                    long playTotalTimestampInMinutes =
                            trackDocument.getPlayTotalTimestampInMinutes() == null
                                    ? 0
                                    : trackDocument.getPlayTotalTimestampInMinutes();
                    trackDocument.setPlayTotalTimestampInMinutes(
                            playTotalTimestampInMinutes
                                    + trackPlayDto.userPlayedAtTimestamp() / 60000);
                    trackDocument.setPopularity(calculatePopularity(trackDocument));
                });
        elasticsearchOperations.save(items);
        //        elasticsearchOperations.bulkUpdate(
        //                trackPlayDtos.stream()
        //                        .map(
        //                                trackPlayDto ->
        //
        // UpdateQuery.builder(trackPlayDto.trackId().toString())
        //                                                .withScript(
        //                                                        "ctx._source.numberOfPlays = "
        //                                                                +
        // trackPlayDto.numberOfPlays()
        //                                                                + ";"
        //                                                                + "if
        // (ctx._source.playTotalTimestampInMinutes == null) { "
        //                                                                +
        // "ctx._source.playTotalTimestampInMinutes = "
        //                                                                + trackPlayDto
        //
        // .userPlayedAtTimestamp()
        //                                                                        / 60000
        //                                                                + "} else { "
        //                                                                +
        // "ctx._source.playTotalTimestampInMinutes += "
        //                                                                + trackPlayDto
        //
        // .userPlayedAtTimestamp()
        //                                                                        / 60000
        //                                                                + "}")
        //                                                .build())
        //                        .toList(),
        //                IndexCoordinates.of("track"));
    }
}
