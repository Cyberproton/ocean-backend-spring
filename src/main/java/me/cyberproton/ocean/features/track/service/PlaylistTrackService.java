package me.cyberproton.ocean.features.track.service;

import lombok.AllArgsConstructor;

import me.cyberproton.ocean.domain.BaseQuery;
import me.cyberproton.ocean.features.playlist.dto.AddPlaylistTrackRequest;
import me.cyberproton.ocean.features.playlist.dto.UpdatePlaylistTrackPositionRequest;
import me.cyberproton.ocean.features.playlist.entity.PlaylistEntity;
import me.cyberproton.ocean.features.playlist.entity.PlaylistTrackEntity;
import me.cyberproton.ocean.features.playlist.repository.PlaylistRepository;
import me.cyberproton.ocean.features.playlist.repository.PlaylistTrackRepository;
import me.cyberproton.ocean.features.track.dto.TrackResponse;
import me.cyberproton.ocean.features.track.entity.TrackEntity;
import me.cyberproton.ocean.features.track.repository.TrackRepository;
import me.cyberproton.ocean.features.track.util.TrackMapper;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@Service
public class PlaylistTrackService {
    private final PlaylistRepository playlistRepository;
    private final TrackRepository trackRepository;
    private final PlaylistTrackRepository playlistTrackRepository;
    private final TrackMapper trackMapper;

    public List<TrackResponse> getPlaylistTracks(Long playlistId, BaseQuery query) {
        return trackRepository
                .findAllByPlaylistTracksPlaylistIdOrderByPlaylistTracksTrackPosition(
                        playlistId, query.toOffsetBasedPageable())
                .map(trackMapper::entityToResponse)
                .toList();
    }

    public void addTracksToPlaylist(Long playlistId, AddPlaylistTrackRequest request) {
        PlaylistEntity playlist = playlistRepository.findById(playlistId).orElseThrow();
        List<TrackEntity> tracks = trackRepository.findAllById(request.trackIds());
        if (tracks.size() != request.trackIds().size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Some tracks do not exist");
        }
        List<PlaylistTrackEntity> playlistTracks =
                playlistTrackRepository
                        .findAllByPlaylistIdOrderByTrackPosition(playlistId)
                        .orElseThrow();
        if (request.position() > playlistTracks.size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Position is out of range");
        }
        playlistTracks.addAll(
                request.position().intValue(),
                tracks.stream()
                        .map(
                                track ->
                                        PlaylistTrackEntity.builder()
                                                .playlist(playlist)
                                                .track(track)
                                                .trackPosition(0L)
                                                .build())
                        .toList());
        // Update track positions
        for (int i = request.position().intValue(); i < playlistTracks.size(); i++) {
            playlistTracks.get(i).setTrackPosition((long) i);
        }
        playlistTrackRepository.saveAll(playlistTracks);
    }

    public void removeTracksFromPlaylist(Long playlistId, Collection<Long> trackIds) {
        playlistRepository.findById(playlistId).orElseThrow();
        List<TrackEntity> tracks = trackRepository.findAllById(trackIds);
        if (tracks.size() != trackIds.size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Some tracks do not exist");
        }
        List<PlaylistTrackEntity> playlistTracks =
                playlistTrackRepository
                        .findAllByPlaylistIdOrderByTrackPosition(playlistId)
                        .orElseThrow();
        playlistTracks.removeIf(
                playlistTrack -> trackIds.contains(playlistTrack.getTrack().getId()));
        // Update track positions
        for (int i = 0; i < playlistTracks.size(); i++) {
            playlistTracks.get(i).setTrackPosition((long) i);
        }
        playlistTrackRepository.saveAll(playlistTracks);
    }

    public void updatePlaylistTracksPosition(
            Long playlistId,
            UpdatePlaylistTrackPositionRequest updatePlaylistTrackPositionRequest) {
        playlistRepository.findById(playlistId).orElseThrow();
        List<PlaylistTrackEntity> playlistTracks =
                playlistTrackRepository
                        .findAllByPlaylistIdOrderByTrackPosition(playlistId)
                        .orElseThrow();
        long startPosition = updatePlaylistTrackPositionRequest.startPosition();
        long insertBeforePosition = updatePlaylistTrackPositionRequest.insertBeforePosition();
        long length = updatePlaylistTrackPositionRequest.length();

        if (startPosition >= playlistTracks.size()
                || insertBeforePosition >= playlistTracks.size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Position is out of range");
        }

        // Remove tracks from the playlist
        List<PlaylistTrackEntity> reorderingTracks = new ArrayList<>();
        for (int i = (int) startPosition; i < startPosition + length; i++) {
            reorderingTracks.add(playlistTracks.get(i));
        }
        playlistTracks.removeAll(reorderingTracks);

        playlistTracks.addAll((int) insertBeforePosition, reorderingTracks);

        // Update track positions
        for (int i = 0; i < playlistTracks.size(); i++) {
            playlistTracks.get(i).setTrackPosition((long) i);
        }
        playlistTrackRepository.saveAll(playlistTracks);
    }
}
