package me.cyberproton.ocean.features.track.service;

import lombok.AllArgsConstructor;

import me.cyberproton.ocean.features.track.repository.TrackAnalyticsRepository;
import me.cyberproton.ocean.features.track.repository.TrackRepository;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class TrackAnalyticsService {
    public final ApplicationEventPublisher eventPublisher;
    private final TrackRepository trackRepository;
    private final TrackAnalyticsRepository trackAnalyticsRepository;

    public void increaseTrackPlayCount(Long trackId) {
        //        TrackEntity track =
        //                trackRepository
        //                        .findById(trackId)
        //                        .orElseThrow(
        //                                () ->
        //                                        new ResponseStatusException(
        //                                                HttpStatus.NOT_FOUND, "Track not found"));
        //        TrackAnalyticsEntity trackAnalytics =
        //                trackAnalyticsRepository
        //                        .findById(trackId)
        //                        .orElseGet(() ->
        // TrackAnalyticsEntity.builder().track(track).build());
        //        trackAnalytics.setNumberOfPlays(trackAnalytics.getNumberOfPlays() + 1);
        //        trackAnalyticsRepository.save(trackAnalytics);
        //        eventPublisher.publishEvent(
        //                new TrackPlayChangeEvent(
        //                        List.of(
        //                                new TrackPlayDto(
        //                                        trackId,
        //                                        trackAnalytics.getNumberOfPlays(),
        //                                        System.currentTimeMillis()))));
    }
}
