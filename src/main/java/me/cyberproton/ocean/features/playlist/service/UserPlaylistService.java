package me.cyberproton.ocean.features.playlist.service;

import lombok.AllArgsConstructor;

import me.cyberproton.ocean.domain.BaseQuery;
import me.cyberproton.ocean.features.playlist.dto.CreateOrUpdatePlaylistRequest;
import me.cyberproton.ocean.features.playlist.dto.PlaylistResponse;
import me.cyberproton.ocean.features.playlist.entity.PlaylistEntity;
import me.cyberproton.ocean.features.playlist.entity.PlaylistTrackEntity;
import me.cyberproton.ocean.features.playlist.entity.SavedPlaylistEntity;
import me.cyberproton.ocean.features.playlist.entity.SavedPlaylistEntityKey;
import me.cyberproton.ocean.features.playlist.event.PlaylistEvent;
import me.cyberproton.ocean.features.playlist.event.PlaylistSavedEvent;
import me.cyberproton.ocean.features.playlist.repository.PlaylistRepository;
import me.cyberproton.ocean.features.playlist.repository.PlaylistViewRepository;
import me.cyberproton.ocean.features.playlist.repository.SavedPlaylistRepository;
import me.cyberproton.ocean.features.playlist.util.PlaylistMapper;
import me.cyberproton.ocean.features.track.entity.TrackEntity;
import me.cyberproton.ocean.features.track.repository.TrackRepository;
import me.cyberproton.ocean.features.user.UserEntity;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserPlaylistService {
    private final PlaylistRepository playlistRepository;
    private final PlaylistViewRepository playlistViewRepository;
    private final SavedPlaylistRepository savedPlaylistRepository;
    private final TrackRepository trackRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final PlaylistMapper playlistMapper;

    public Set<PlaylistResponse> getUserPlaylists(Long userId, BaseQuery query) {
        return playlistViewRepository
                .findAllByOwnerId(userId, query.toOffsetBasedPageable(Sort.by("id")))
                .stream()
                .map(playlistMapper::viewToResponse)
                .collect(Collectors.toSet());
        //        return playlistRepository
        //                .findByOwnerId(userId)
        //                .map(
        //                        p ->
        //                                p.stream()
        //                                        .map(playlistMapper::entityToResponse)
        //                                        .collect(Collectors.toSet()))
        //                .orElseThrow();
    }

    public PlaylistResponse createUserPlaylist(
            UserEntity user, CreateOrUpdatePlaylistRequest createOrUpdatePlaylist) {
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
        PlaylistEntity playlist =
                PlaylistEntity.builder()
                        .name(createOrUpdatePlaylist.name())
                        .description(createOrUpdatePlaylist.description())
                        .isPublic(createOrUpdatePlaylist.isPublic())
                        .owner(user)
                        .playlistTracks(tracks)
                        .build();
        PlaylistEntity saved = playlistRepository.save(playlist);
        eventPublisher.publishEvent(new PlaylistEvent(PlaylistEvent.Type.CREATE, saved));
        return playlistMapper.entityToResponse(saved);
    }

    public void savePlaylists(UserEntity user, Collection<Long> playlistIds) {
        List<PlaylistEntity> playlists = playlistRepository.findAllById(playlistIds);
        if (playlists.size() != playlistIds.size()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Some playlists do not exist");
        }
        playlists.forEach(playlist -> playlist.addSavedUser(user));
        playlistRepository.saveAll(playlists);

        long currentTimestamp = System.currentTimeMillis();
        List<PlaylistSavedEvent.SavedPlaylist> savedPlaylists =
                playlists.stream()
                        .map(
                                playlist ->
                                        new PlaylistSavedEvent.SavedPlaylist(
                                                playlist.getId(),
                                                PlaylistSavedEvent.Type.SAVED,
                                                currentTimestamp))
                        .toList();
        eventPublisher.publishEvent(
                new PlaylistSavedEvent(savedPlaylists, PlaylistSavedEvent.Type.SAVED));
    }

    public void deleteSavedPlaylists(UserEntity user, Collection<Long> playlistIds) {
        List<SavedPlaylistEntityKey> savedPlaylistEntityKeys =
                playlistIds.stream()
                        .map(playlistId -> new SavedPlaylistEntityKey(user.getId(), playlistId))
                        .toList();
        List<SavedPlaylistEntity> savedPlaylistEntities =
                savedPlaylistRepository.findAllById(savedPlaylistEntityKeys);
        if (savedPlaylistEntities.size() != playlistIds.size()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Some playlists do not exist");
        }
        savedPlaylistRepository.deleteAll(savedPlaylistEntities);
        List<PlaylistSavedEvent.SavedPlaylist> savedPlaylists =
                savedPlaylistEntities.stream()
                        .map(
                                playlist ->
                                        new PlaylistSavedEvent.SavedPlaylist(
                                                playlist.getId().getPlaylistId(),
                                                PlaylistSavedEvent.Type.UNSAVED,
                                                playlist.getSavedAt().getTime()))
                        .toList();
        eventPublisher.publishEvent(
                new PlaylistSavedEvent(savedPlaylists, PlaylistSavedEvent.Type.UNSAVED));
    }
}
