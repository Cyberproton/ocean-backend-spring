package me.cyberproton.ocean.features.track.event;

import lombok.AllArgsConstructor;

import me.cyberproton.ocean.features.track.dto.TrackLikeDto;
import me.cyberproton.ocean.features.track.dto.TrackPlayDto;
import me.cyberproton.ocean.features.track.entity.TrackAnalyticsEntity;
import me.cyberproton.ocean.features.track.entity.TrackEntity;
import me.cyberproton.ocean.features.track.repository.TrackAnalyticsRepository;
import me.cyberproton.ocean.features.track.util.TrackUtils;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Component
public class TrackListener {
    private final TrackAnalyticsRepository trackAnalyticsRepository;

    @Async
    @EventListener
    public void onTracksLikeChange(TracksLikeChangeEvent event) {
        List<Long> trackIds = event.getTrackLikeDtos().stream().map(TrackLikeDto::trackId).toList();
        List<TrackAnalyticsEntity> trackAnalyticsList =
                trackAnalyticsRepository.findAllById(trackIds);
        // In case the track analytics is not found in the database, create a new one
        List<TrackAnalyticsEntity> newTrackAnalyticsList = new ArrayList<>();
        for (TrackLikeDto trackLikeDto : event.getTrackLikeDtos()) {
            TrackAnalyticsEntity trackAnalytics =
                    trackAnalyticsList.stream()
                            .filter(
                                    trackAnalyticsEntity ->
                                            trackAnalyticsEntity
                                                    .getId()
                                                    .equals(trackLikeDto.trackId()))
                            .findFirst()
                            .orElseGet(
                                    () ->
                                            TrackAnalyticsEntity.builder()
                                                    .id(trackLikeDto.trackId())
                                                    .track(
                                                            TrackEntity.builder()
                                                                    .id(trackLikeDto.trackId())
                                                                    .build())
                                                    .build());
            newTrackAnalyticsList.add(trackAnalytics);

            long likeTotalTimestampInMinutes =
                    trackAnalytics.getLikeTotalTimestampInMinutes() == null
                            ? 0
                            : trackAnalytics.getLikeTotalTimestampInMinutes();
            trackAnalytics.setNumberOfLikes(trackLikeDto.numberOfLikes());
            trackAnalytics.setLikeTotalTimestampInMinutes(
                    likeTotalTimestampInMinutes
                            + (trackLikeDto.userLikedAtTimestamp() / 60000)
                                    * (trackLikeDto.type() == TrackLikeDto.LikeType.LIKE ? 1 : -1));
            trackAnalytics.setPopularity(TrackUtils.calculatePopularity(trackAnalytics));
        }
        trackAnalyticsRepository.saveAll(newTrackAnalyticsList);
    }

    @Async
    @EventListener
    public void onTrackPlayChange(TrackPlayChangeEvent event) {
        List<Long> trackIds = event.getTrackPlayDtos().stream().map(TrackPlayDto::trackId).toList();
        List<TrackAnalyticsEntity> trackAnalyticsList =
                trackAnalyticsRepository.findAllById(trackIds);
        // In case the track analytics is not found in the database, create a new one
        List<TrackAnalyticsEntity> newTrackAnalyticsList = new ArrayList<>();
        for (TrackPlayDto trackPlayDto : event.getTrackPlayDtos()) {
            TrackAnalyticsEntity trackAnalytics =
                    trackAnalyticsList.stream()
                            .filter(
                                    trackAnalyticsEntity ->
                                            trackAnalyticsEntity
                                                    .getId()
                                                    .equals(trackPlayDto.trackId()))
                            .findFirst()
                            .orElseGet(
                                    () ->
                                            TrackAnalyticsEntity.builder()
                                                    .id(trackPlayDto.trackId())
                                                    .track(
                                                            TrackEntity.builder()
                                                                    .id(trackPlayDto.trackId())
                                                                    .build())
                                                    .build());
            newTrackAnalyticsList.add(trackAnalytics);

            long playTotalTimestampInMinutes =
                    trackAnalytics.getPlayTotalTimestampInMinutes() == null
                            ? 0
                            : trackAnalytics.getPlayTotalTimestampInMinutes();
            trackAnalytics.setNumberOfPlays(trackPlayDto.numberOfPlays());
            trackAnalytics.setPlayTotalTimestampInMinutes(
                    playTotalTimestampInMinutes + (trackPlayDto.userPlayedAtTimestamp() / 60000));
            trackAnalytics.setPopularity(TrackUtils.calculatePopularity(trackAnalytics));
        }
        trackAnalyticsRepository.saveAll(newTrackAnalyticsList);
    }
}
