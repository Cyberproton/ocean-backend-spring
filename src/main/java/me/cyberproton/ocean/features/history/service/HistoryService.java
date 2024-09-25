package me.cyberproton.ocean.features.history.service;

import com.cosium.spring.data.jpa.entity.graph.domain2.DynamicEntityGraph;
import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;

import jakarta.persistence.criteria.Predicate;

import lombok.RequiredArgsConstructor;

import me.cyberproton.ocean.config.ExternalAppConfig;
import me.cyberproton.ocean.domain.PaginationResponse;
import me.cyberproton.ocean.features.album.entity.AlbumEntity;
import me.cyberproton.ocean.features.album.repository.AlbumRepository;
import me.cyberproton.ocean.features.artist.ArtistEntity;
import me.cyberproton.ocean.features.artist.ArtistRepository;
import me.cyberproton.ocean.features.history.dto.CreateHistoryRequest;
import me.cyberproton.ocean.features.history.dto.HistoryQuery;
import me.cyberproton.ocean.features.history.dto.HistoryResponse;
import me.cyberproton.ocean.features.history.entity.HistoryEntity;
import me.cyberproton.ocean.features.history.entity.HistoryEntity_;
import me.cyberproton.ocean.features.history.entity.HistoryType;
import me.cyberproton.ocean.features.history.repository.HistoryRepository;
import me.cyberproton.ocean.features.history.repository.HistoryViewRepository;
import me.cyberproton.ocean.features.history.util.HistoryMapper;
import me.cyberproton.ocean.features.playlist.entity.PlaylistEntity;
import me.cyberproton.ocean.features.playlist.event.PlaylistPlayedEvent;
import me.cyberproton.ocean.features.playlist.repository.PlaylistRepository;
import me.cyberproton.ocean.features.track.dto.TrackPlayDto;
import me.cyberproton.ocean.features.track.entity.TrackEntity;
import me.cyberproton.ocean.features.track.event.TrackPlayChangeEvent;
import me.cyberproton.ocean.features.track.repository.TrackRepository;
import me.cyberproton.ocean.features.user.UserEntity;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class HistoryService {
    private final HistoryRepository historyRepository;
    private final TrackRepository trackRepository;
    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;
    private final PlaylistRepository playlistRepository;
    private final ExternalAppConfig externalAppConfig;
    private final HistoryMapper historyMapper;
    private final HistoryViewRepository historyViewRepository;
    private final ApplicationEventPublisher eventPublisher;

    public PaginationResponse<HistoryResponse> getHistories(UserEntity user, HistoryQuery query) {
        Page<HistoryResponse> page =
                historyViewRepository
                        .findAll(
                                (rootAlias, builder) -> {
                                    builder.where(HistoryEntity_.USER).eq(user);
                                    if (query.getType() != null) {
                                        builder.where(HistoryEntity_.TYPE).eq(query.getType());
                                    }
                                },
                                query.toOffsetBasedPageable(Sort.by("id")))
                        .map(historyMapper::viewToResponse);
        return PaginationResponse.fromPage(
                page,
                UriComponentsBuilder.fromHttpUrl(externalAppConfig.domain())
                        .pathSegment(externalAppConfig.apiV1Path())
                        .pathSegment("histories")
                        .toUriString());
    }

    public HistoryResponse getHistory(Long id) {
        HistoryEntity history =
                historyRepository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new ResponseStatusException(
                                                HttpStatus.NOT_FOUND, "History not found"));
        return historyMapper.entityToResponse(history);
    }

    public HistoryResponse createHistory(UserEntity user, CreateHistoryRequest request) {
        TrackEntity track = null;
        AlbumEntity album = null;
        ArtistEntity artist = null;
        PlaylistEntity playlist = null;

        HistoryEntity existingHistory =
                historyRepository
                        .findFirstByUserIdAndTrackIdAndAlbumIdAndArtistIdAndPlaylistIdOrderByUpdatedAtDesc(
                                user.getId(),
                                request.trackId(),
                                request.albumId(),
                                request.artistId(),
                                request.playlistId())
                        .orElse(null);

        if (existingHistory != null) {
            // If the request is to override the old record, update the updatedAt field and return
            // the existing history
            if (request.type().isOverrideOldRecord()) {
                existingHistory.setUpdatedAt(new Date());
                historyRepository.save(existingHistory);
                return historyMapper.entityToResponse(existingHistory);
            }

            // If the request is to create a new record after a certain timestamp, check if the
            // existing record is still valid
            if (request.type().getCreateNewRecordAfterTimestamp() > 0
                    && System.currentTimeMillis() - existingHistory.getUpdatedAt().getTime()
                            < request.type().getCreateNewRecordAfterTimestamp()) {
                return historyMapper.entityToResponse(existingHistory);
            }
        }

        // Create new record after the check

        if (request.type() == HistoryType.TRACK) {
            track =
                    trackRepository
                            .findById(request.trackId())
                            .orElseThrow(
                                    () ->
                                            new ResponseStatusException(
                                                    HttpStatus.NOT_FOUND, "Track not found"));

        } else if (request.type() == HistoryType.ALBUM) {
            album =
                    albumRepository
                            .findById(request.albumId())
                            .orElseThrow(
                                    () ->
                                            new ResponseStatusException(
                                                    HttpStatus.NOT_FOUND, "Album not found"));
        } else if (request.type() == HistoryType.ARTIST) {
            artist =
                    artistRepository
                            .findById(request.artistId())
                            .orElseThrow(
                                    () ->
                                            new ResponseStatusException(
                                                    HttpStatus.NOT_FOUND, "Artist not found"));
        } else if (request.type() == HistoryType.PLAYLIST) {
            playlist =
                    playlistRepository
                            .findById(request.playlistId())
                            .orElseThrow(
                                    () ->
                                            new ResponseStatusException(
                                                    HttpStatus.NOT_FOUND, "Playlist not found"));
        }

        HistoryEntity history =
                HistoryEntity.builder()
                        .type(request.type())
                        .user(user)
                        .album(album)
                        .artist(artist)
                        .playlist(playlist)
                        .track(track)
                        .updatedAt(new Date())
                        .build();

        historyRepository.save(history);

        if (request.type() == HistoryType.TRACK) {
            long numberOfPlays = historyRepository.countByTrackId(track.getId());
            eventPublisher.publishEvent(
                    new TrackPlayChangeEvent(
                            List.of(
                                    new TrackPlayDto(
                                            track.getId(),
                                            track.getAlbum() != null
                                                    ? track.getAlbum().getId()
                                                    : null,
                                            numberOfPlays,
                                            System.currentTimeMillis()))));
        }

        if (request.type() == HistoryType.PLAYLIST) {
            eventPublisher.publishEvent(
                    new PlaylistPlayedEvent(
                            List.of(
                                    new PlaylistPlayedEvent.PlayedPlaylist(
                                            playlist.getId(), System.currentTimeMillis()))));
        }

        return historyMapper.entityToResponse(history);
    }

    public void deleteHistory(Long id) {
        historyRepository.deleteById(id);
    }

    private Specification<HistoryEntity> createSpecification(HistoryQuery query, Long userId) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            // Predicates
            List<Predicate> predicates = new ArrayList<>();
            if (userId != null) {
                predicates.add(criteriaBuilder.equal(root.get("user").get("id"), userId));
            }
            if (query.getType() != null) {
                predicates.add(criteriaBuilder.equal(root.get("type"), query.getType()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private EntityGraph createEntityGraph() {
        return DynamicEntityGraph.fetching()
                .addPath("artist")
                .addPath("artist.user")
                .addPath("artist.user.profile")
                .addPath("album")
                .addPath("album.covers")
                .addPath("playlist")
                .addPath("track")
                .addPath("user")
                .build();
    }
}
