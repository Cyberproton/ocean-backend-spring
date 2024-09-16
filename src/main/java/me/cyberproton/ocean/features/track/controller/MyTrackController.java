package me.cyberproton.ocean.features.track.controller;

import jakarta.validation.Valid;

import lombok.AllArgsConstructor;

import me.cyberproton.ocean.annotations.V1ApiRestController;
import me.cyberproton.ocean.config.AppUserDetails;
import me.cyberproton.ocean.domain.BaseQuery;
import me.cyberproton.ocean.features.track.dto.LikeOrUnlikeTracksRequest;
import me.cyberproton.ocean.features.track.dto.TrackResponse;
import me.cyberproton.ocean.features.track.service.UserTrackService;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@V1ApiRestController
@RequestMapping("/users/me/tracks")
public class MyTrackController {
    private final UserTrackService userTrackService;

    @GetMapping("liked")
    public List<TrackResponse> getLikedTracks(
            @AuthenticationPrincipal AppUserDetails userDetails, @Valid BaseQuery query) {
        return userTrackService.getLikedTracks(userDetails.getUser(), query);
    }

    @PostMapping("liked")
    public void likeTracks(
            @AuthenticationPrincipal AppUserDetails userDetails,
            @Valid @RequestBody LikeOrUnlikeTracksRequest request) {
        userTrackService.likeTracks(userDetails.getUser(), request.getTrackIds());
    }

    @DeleteMapping("liked")
    public void unlikeTracks(
            @AuthenticationPrincipal AppUserDetails userDetails,
            @Valid @RequestBody LikeOrUnlikeTracksRequest request) {
        userTrackService.unlikeTracks(userDetails.getUser(), request.getTrackIds());
    }
}
