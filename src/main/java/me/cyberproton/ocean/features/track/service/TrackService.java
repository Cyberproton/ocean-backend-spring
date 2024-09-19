package me.cyberproton.ocean.features.track.service;

import lombok.AllArgsConstructor;

import me.cyberproton.ocean.config.ExternalAppConfig;
import me.cyberproton.ocean.domain.BaseQuery;
import me.cyberproton.ocean.domain.PaginationResponse;
import me.cyberproton.ocean.features.album.entity.AlbumEntity;
import me.cyberproton.ocean.features.album.repository.AlbumRepository;
import me.cyberproton.ocean.features.artist.ArtistEntity;
import me.cyberproton.ocean.features.artist.ArtistRepository;
import me.cyberproton.ocean.features.track.dto.CreateOrUpdateTrackRequest;
import me.cyberproton.ocean.features.track.dto.TrackResponse;
import me.cyberproton.ocean.features.track.entity.TrackEntity;
import me.cyberproton.ocean.features.track.event.TrackEvent;
import me.cyberproton.ocean.features.track.repository.TrackElasticsearchRepository;
import me.cyberproton.ocean.features.track.repository.TrackRepository;
import me.cyberproton.ocean.features.track.repository.TrackViewRepository;
import me.cyberproton.ocean.features.track.util.TrackMapper;
import me.cyberproton.ocean.features.track.view.TrackView;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Set;

@AllArgsConstructor
@Service
public class TrackService {
    private final TrackRepository trackRepository;
    private final TrackViewRepository trackViewRepository;
    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final ExternalAppConfig externalAppConfig;
    private final TrackMapper trackMapper;
    private final TrackElasticsearchRepository trackElasticsearchRepository;

    public PaginationResponse<TrackResponse> getTracks(BaseQuery query) {
        Pageable pageable = query.toOffsetBasedPageable();

        return PaginationResponse.fromPage(
                trackRepository.findAllEagerly(pageable).map(trackMapper::entityToResponse),
                UriComponentsBuilder.fromHttpUrl(externalAppConfig.domain())
                        .pathSegment(externalAppConfig.apiV1Path())
                        .pathSegment("tracks")
                        .toUriString());
    }

    public TrackResponse getTrackById(Long id) {
        return trackRepository.findById(id).map(trackMapper::entityToResponse).orElse(null);
    }

    public TrackResponse createTrack(CreateOrUpdateTrackRequest request) {
        AlbumEntity album =
                albumRepository
                        .findById(request.albumId())
                        .orElseThrow(
                                () ->
                                        new ResponseStatusException(
                                                HttpStatus.NOT_FOUND, "Album not found"));
        Set<ArtistEntity> artists = Set.copyOf(artistRepository.findAllById(request.artistIds()));
        if (artists.size() != request.artistIds().size()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Some artists not found");
        }
        TrackEntity track =
                TrackEntity.builder()
                        .name(request.name())
                        .trackNumber(request.trackNumber())
                        .duration(request.duration())
                        .album(album)
                        .artists(artists)
                        .build();
        eventPublisher.publishEvent(new TrackEvent(TrackEvent.Type.CREATE, track));
        TrackEntity saved = trackRepository.save(track);
        return trackMapper.entityToResponse(saved);
    }

    public TrackResponse updateTrack(Long id, CreateOrUpdateTrackRequest request) {
        TrackEntity track =
                trackRepository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new ResponseStatusException(
                                                HttpStatus.NOT_FOUND, "Track not found"));
        AlbumEntity album =
                albumRepository
                        .findById(request.albumId())
                        .orElseThrow(
                                () ->
                                        new ResponseStatusException(
                                                HttpStatus.NOT_FOUND, "Album not found"));
        Set<ArtistEntity> artists = Set.copyOf(artistRepository.findAllById(request.artistIds()));
        if (artists.size() != request.artistIds().size()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Some artists not found");
        }
        track.setName(request.name());
        track.setTrackNumber(request.trackNumber());
        track.setDuration(request.duration());
        track.setAlbum(album);
        track.setArtists(artists);
        TrackEntity saved = trackRepository.save(track);
        eventPublisher.publishEvent(new TrackEvent(TrackEvent.Type.UPDATE, saved));
        return trackMapper.entityToResponse(saved);
    }

    public Long deleteTrack(Long id) {
        TrackEntity track =
                trackRepository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new ResponseStatusException(
                                                HttpStatus.NOT_FOUND, "Track not found"));
        trackRepository.deleteById(id);
        eventPublisher.publishEvent(new TrackEvent(TrackEvent.Type.DELETE, track));
        return id;
    }

    public PaginationResponse<TrackResponse> getTopTracks(BaseQuery query) {
        Pageable pageable =
                query.toOffsetBasedPageable(
                        Sort.by(Sort.Order.desc("popularity"), Sort.Order.asc("id")));
        Page<TrackView> page = trackViewRepository.findAll(pageable);
        return PaginationResponse.fromPage(
                page.map(trackMapper::viewToResponse),
                UriComponentsBuilder.fromHttpUrl(externalAppConfig.domain())
                        .pathSegment(externalAppConfig.apiV1Path())
                        .pathSegment("tracks")
                        .pathSegment("top")
                        .toUriString());
    }
}
