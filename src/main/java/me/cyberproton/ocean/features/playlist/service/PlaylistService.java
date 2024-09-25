package me.cyberproton.ocean.features.playlist.service;

import lombok.AllArgsConstructor;

import me.cyberproton.ocean.domain.BaseQuery;
import me.cyberproton.ocean.features.playlist.dto.CreateOrUpdatePlaylistRequest;
import me.cyberproton.ocean.features.playlist.dto.PlaylistResponse;
import me.cyberproton.ocean.features.playlist.entity.PlaylistEntity;
import me.cyberproton.ocean.features.playlist.entity.PlaylistTrackEntity;
import me.cyberproton.ocean.features.playlist.event.PlaylistEvent;
import me.cyberproton.ocean.features.playlist.repository.PlaylistRepository;
import me.cyberproton.ocean.features.playlist.repository.PlaylistViewRepository;
import me.cyberproton.ocean.features.playlist.util.PlaylistMapper;
import me.cyberproton.ocean.features.track.entity.TrackEntity;
import me.cyberproton.ocean.features.track.repository.TrackRepository;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class PlaylistService {
    private final PlaylistRepository playlistRepository;
    private final PlaylistViewRepository playlistViewRepository;
    private final TrackRepository trackRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final PlaylistMapper playlistMapper;

    public PlaylistResponse updatePlaylist(
            Long playlistId, CreateOrUpdatePlaylistRequest createOrUpdatePlaylist) {
        Set<PlaylistTrackEntity> tracks = null;
        if (createOrUpdatePlaylist.trackIds() != null) {
            List<TrackEntity> ts = trackRepository.findAllById(createOrUpdatePlaylist.trackIds());
            if (ts.size() != createOrUpdatePlaylist.trackIds().size()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Some tracks do not exist");
            }
            tracks =
                    ts.stream()
                            .map(
                                    track ->
                                            PlaylistTrackEntity.builder()
                                                    .track(track)
                                                    .trackPosition(0L)
                                                    .build())
                            .collect(Collectors.toSet());
        }
        PlaylistEntity playlist = playlistRepository.findById(playlistId).orElseThrow();
        playlist.setName(createOrUpdatePlaylist.name());
        playlist.setDescription(createOrUpdatePlaylist.description());
        playlist.setPublic(createOrUpdatePlaylist.isPublic());
        playlist.setPlaylistTracks(tracks);
        PlaylistEntity saved = playlistRepository.save(playlist);
        eventPublisher.publishEvent(new PlaylistEvent(PlaylistEvent.Type.UPDATE, saved));
        return playlistMapper.entityToResponse(saved);
    }

    public void deletePlaylist(Long playlistId) {
        PlaylistEntity playlist = playlistRepository.findById(playlistId).orElseThrow();
        playlistRepository.deleteById(playlistId);
        eventPublisher.publishEvent(new PlaylistEvent(PlaylistEvent.Type.DELETE, playlist));
    }

    public Page<PlaylistResponse> getTopPlaylists(BaseQuery query) {
        Pageable pageable =
                query.toOffsetBasedPageable(
                        Sort.by(Sort.Order.desc("popularity"), Sort.Order.asc("id")));
        return playlistViewRepository.findAll(pageable).map(playlistMapper::viewToResponse);
    }
}
