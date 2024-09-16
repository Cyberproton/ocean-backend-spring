package me.cyberproton.ocean.features.track.service;

import lombok.AllArgsConstructor;

import me.cyberproton.ocean.features.track.entity.TrackAnalyticsEntity;
import me.cyberproton.ocean.features.track.entity.TrackEntity;
import me.cyberproton.ocean.features.track.repository.TrackAnalyticsRepository;
import me.cyberproton.ocean.features.track.repository.TrackRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@AllArgsConstructor
@Service
public class TrackAnalyticsService {
    private final TrackRepository trackRepository;
    private final TrackAnalyticsRepository trackAnalyticsRepository;

    public void increaseTrackPlayCount(Long trackId) {
        TrackEntity track =
                trackRepository
                        .findById(trackId)
                        .orElseThrow(
                                () ->
                                        new ResponseStatusException(
                                                HttpStatus.NOT_FOUND, "Track not found"));
        TrackAnalyticsEntity trackAnalytics =
                trackAnalyticsRepository
                        .findById(trackId)
                        .orElseGet(() -> TrackAnalyticsEntity.builder().track(track).build());
        trackAnalytics.setNumberOfPlays(trackAnalytics.getNumberOfPlays() + 1);
        trackAnalyticsRepository.save(trackAnalytics);
    }
}
