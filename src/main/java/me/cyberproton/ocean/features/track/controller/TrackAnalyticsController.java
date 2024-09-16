package me.cyberproton.ocean.features.track.controller;

import lombok.AllArgsConstructor;

import me.cyberproton.ocean.annotations.V1ApiRestController;
import me.cyberproton.ocean.features.track.service.TrackAnalyticsService;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@AllArgsConstructor
@V1ApiRestController
@RequestMapping("/analytics/tracks")
public class TrackAnalyticsController {
    private final TrackAnalyticsService trackAnalyticsService;

    @PatchMapping("/{trackId}/play")
    public void increaseTrackPlayCount(@PathVariable("trackId") Long trackId) {
        trackAnalyticsService.increaseTrackPlayCount(trackId);
    }
}
