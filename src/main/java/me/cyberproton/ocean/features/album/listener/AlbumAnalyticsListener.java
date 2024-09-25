package me.cyberproton.ocean.features.album.listener;

import lombok.AllArgsConstructor;

import me.cyberproton.ocean.features.album.entity.AlbumAnalyticsEntity;
import me.cyberproton.ocean.features.album.entity.AlbumEntity;
import me.cyberproton.ocean.features.album.event.AlbumSavedEvent;
import me.cyberproton.ocean.features.album.repository.AlbumAnalyticsRepository;
import me.cyberproton.ocean.features.track.dto.TrackPlayDto;
import me.cyberproton.ocean.features.track.event.TrackPlayChangeEvent;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
public class AlbumAnalyticsListener {
    private final AlbumAnalyticsRepository albumAnalyticsRepository;

    @EventListener
    public void onTrackPlayedEvent(TrackPlayChangeEvent event) {
        List<Long> trackIds = event.getTrackPlayDtos().stream().map(TrackPlayDto::trackId).toList();
        List<AlbumAnalyticsEntity> analyticsEntityList =
                albumAnalyticsRepository.findAllByAlbumTracksIdIn(trackIds);
        List<AlbumAnalyticsEntity> newAnalyticsEntityList =
                event.getTrackPlayDtos().stream()
                        .map(
                                trackPlayDto -> {
                                    long playedAt = trackPlayDto.userPlayedAtTimestamp();
                                    AlbumAnalyticsEntity albumAnalyticsEntity =
                                            analyticsEntityList.stream()
                                                    .filter(
                                                            analyticsEntity ->
                                                                    analyticsEntity
                                                                            .getId()
                                                                            .equals(
                                                                                    trackPlayDto
                                                                                            .albumId()))
                                                    .findFirst()
                                                    .orElseGet(
                                                            () ->
                                                                    AlbumAnalyticsEntity.builder()
                                                                            .album(
                                                                                    AlbumEntity
                                                                                            .builder()
                                                                                            .id(
                                                                                                    trackPlayDto
                                                                                                            .albumId())
                                                                                            .build())
                                                                            .build());
                                    albumAnalyticsEntity.setNumberOfTrackPlays(
                                            albumAnalyticsEntity.getNumberOfTrackPlays() + 1);
                                    albumAnalyticsEntity.setTrackPlayTotalTimestampInMinutes(
                                            albumAnalyticsEntity
                                                            .getTrackPlayTotalTimestampInMinutes()
                                                    + (playedAt / 60000));
                                    return albumAnalyticsEntity;
                                })
                        .toList();
        albumAnalyticsRepository.saveAll(newAnalyticsEntityList);
    }

    @EventListener
    public void onAlbumSavedEvent(AlbumSavedEvent event) {
        List<Long> albumIds =
                event.getAlbums().stream().map(AlbumSavedEvent.SavedAlbum::albumId).toList();
        List<AlbumAnalyticsEntity> analyticsEntityList =
                albumAnalyticsRepository.findAllById(albumIds);
        List<AlbumAnalyticsEntity> newAnalyticsEntityList =
                event.getAlbums().stream()
                        .map(
                                playedAlbum -> {
                                    long savedAt = playedAlbum.savedAt();
                                    AlbumAnalyticsEntity albumAnalyticsEntity =
                                            analyticsEntityList.stream()
                                                    .filter(
                                                            analyticsEntity ->
                                                                    analyticsEntity
                                                                            .getId()
                                                                            .equals(
                                                                                    playedAlbum
                                                                                            .albumId()))
                                                    .findFirst()
                                                    .orElseGet(
                                                            () ->
                                                                    AlbumAnalyticsEntity.builder()
                                                                            .album(
                                                                                    AlbumEntity
                                                                                            .builder()
                                                                                            .id(
                                                                                                    playedAlbum
                                                                                                            .albumId())
                                                                                            .build())
                                                                            .build());
                                    albumAnalyticsEntity.setNumberOfSaves(
                                            albumAnalyticsEntity.getNumberOfSaves() + 1);
                                    albumAnalyticsEntity.setSaveTotalTimestampInMinutes(
                                            albumAnalyticsEntity.getSaveTotalTimestampInMinutes()
                                                    + (savedAt / 60000));
                                    return albumAnalyticsEntity;
                                })
                        .toList();
        albumAnalyticsRepository.saveAll(newAnalyticsEntityList);
    }
}
