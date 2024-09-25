package me.cyberproton.ocean.features.album.controller;

import jakarta.validation.Valid;

import lombok.AllArgsConstructor;

import me.cyberproton.ocean.annotations.V1ApiRestController;
import me.cyberproton.ocean.config.AppUserDetails;
import me.cyberproton.ocean.domain.BaseQuery;
import me.cyberproton.ocean.features.album.dto.AddOrRemoveSavedAlbumsRequest;
import me.cyberproton.ocean.features.album.service.UserAlbumService;
import me.cyberproton.ocean.features.album.view.AlbumView;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@V1ApiRestController
@RequestMapping("/users/me/albums")
public class MyAlbumController {
    private final UserAlbumService userAlbumService;

    @GetMapping("saved")
    public List<AlbumView> getSavedAlbums(
            @AuthenticationPrincipal AppUserDetails userDetails, BaseQuery query) {
        return userAlbumService.getSavedAlbums(userDetails.getUser(), query);
    }

    @PostMapping("saved")
    public void savePlaylists(
            @AuthenticationPrincipal AppUserDetails userDetails,
            @Valid @RequestBody AddOrRemoveSavedAlbumsRequest request) {
        userAlbumService.saveAlbums(userDetails.getUser(), request.getAlbumIds());
    }

    @DeleteMapping("saved")
    public void deleteSavedPlaylists(
            @AuthenticationPrincipal AppUserDetails userDetails,
            @Valid @RequestBody AddOrRemoveSavedAlbumsRequest request) {
        userAlbumService.deleteSavedAlbums(userDetails.getUser(), request.getAlbumIds());
    }
}
