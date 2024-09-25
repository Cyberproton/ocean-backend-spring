package me.cyberproton.ocean.features.album.controller;

import jakarta.validation.Valid;

import lombok.AllArgsConstructor;

import me.cyberproton.ocean.annotations.V1ApiRestController;
import me.cyberproton.ocean.config.ExternalAppConfig;
import me.cyberproton.ocean.domain.BaseQuery;
import me.cyberproton.ocean.domain.PaginationResponse;
import me.cyberproton.ocean.features.album.dto.AlbumResponse;
import me.cyberproton.ocean.features.album.dto.CreateOrUpdateAlbumRequest;
import me.cyberproton.ocean.features.album.service.AlbumService;
import me.cyberproton.ocean.features.album.service.UserAlbumService;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Set;

@AllArgsConstructor
@V1ApiRestController
@RequestMapping("/albums")
public class AlbumController {
    private final AlbumService albumService;
    private final UserAlbumService userAlbumService;
    private final ExternalAppConfig externalAppConfig;

    @GetMapping
    public Set<AlbumResponse> getAlbums() {
        return albumService.getAlbums();
    }

    @GetMapping("/{id}")
    public AlbumResponse getAlbum(@PathVariable Long id) {
        return albumService.getAlbum(id);
    }

    @PostMapping
    public AlbumResponse createAlbum(@Valid @RequestBody CreateOrUpdateAlbumRequest request) {
        return albumService.createAlbum(request);
    }

    @PutMapping("/{id}")
    public AlbumResponse updateAlbum(
            @PathVariable Long id, @Valid @RequestBody CreateOrUpdateAlbumRequest request) {
        return albumService.updateAlbum(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteAlbum(@PathVariable Long id) {
        albumService.deleteAlbum(id);
    }

    @GetMapping("top")
    public PaginationResponse<AlbumResponse> getTopAlbums(BaseQuery query) {
        return PaginationResponse.fromPage(
                albumService.getTopAlbums(query),
                UriComponentsBuilder.fromHttpUrl(externalAppConfig.domain())
                        .pathSegment(externalAppConfig.apiV1Path())
                        .pathSegment("albums")
                        .pathSegment("top")
                        .toUriString());
    }
}
