package me.cyberproton.ocean.features.track.controller;

import jakarta.validation.Valid;

import lombok.AllArgsConstructor;

import me.cyberproton.ocean.annotations.V1ApiRestController;
import me.cyberproton.ocean.domain.BaseQuery;
import me.cyberproton.ocean.domain.PaginationResponse;
import me.cyberproton.ocean.features.track.dto.CreateOrUpdateTrackRequest;
import me.cyberproton.ocean.features.track.dto.TrackResponse;
import me.cyberproton.ocean.features.track.service.TrackService;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@V1ApiRestController
@RequestMapping("/tracks")
@Validated
public class TrackController {
    private final TrackService trackService;

    @GetMapping
    public PaginationResponse<TrackResponse> getTracks(BaseQuery query) {
        return trackService.getTracks(query);
    }

    @GetMapping("/{id}")
    public TrackResponse getTrack(@PathVariable Long id) {
        return trackService.getTrackById(id);
    }

    @PostMapping
    public TrackResponse createTrack(@Valid @RequestBody CreateOrUpdateTrackRequest request) {
        return trackService.createTrack(request);
    }

    @PutMapping("/{id}")
    public TrackResponse updateTrack(
            @PathVariable Long id, @Valid @RequestBody CreateOrUpdateTrackRequest request) {
        return trackService.updateTrack(id, request);
    }

    @DeleteMapping("/{id}")
    public Long deleteTrack(@PathVariable Long id) {
        return trackService.deleteTrack(id);
    }

    @GetMapping("/top")
    public PaginationResponse<TrackResponse> getTopTracks(BaseQuery query) {
        return trackService.getTopTracks(query);
    }
}
