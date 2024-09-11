package me.cyberproton.ocean.features.track.service;

import lombok.AllArgsConstructor;

import me.cyberproton.ocean.domain.BaseQuery;
import me.cyberproton.ocean.features.track.dto.TrackResponse;
import me.cyberproton.ocean.features.track.entity.TrackEntity;
import me.cyberproton.ocean.features.track.repository.TrackRepository;
import me.cyberproton.ocean.features.track.repository.TrackViewRepository;
import me.cyberproton.ocean.features.track.util.TrackMapper;
import me.cyberproton.ocean.features.user.UserEntity;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@Service
public class UserTrackService {
    private final TrackRepository trackRepository;
    private final TrackViewRepository trackViewRepository;
    private final TrackMapper trackMapper;

    public List<TrackResponse> getLikedTracks(UserEntity user, BaseQuery query) {
        return trackViewRepository.findAllByLikedUsersContains(user, query).stream()
                .map(trackMapper::viewToResponse)
                .toList();
    }

    public void likeTracks(UserEntity user, Collection<Long> trackIds) {
        Collection<TrackEntity> tracks = trackRepository.findAllById(trackIds);
        if (tracks.size() != trackIds.size()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Some tracks do not exist");
        }
        tracks.forEach(track -> track.addLikedUser(user));
        trackRepository.saveAll(tracks);
    }

    public void unlikeTracks(UserEntity user, Collection<Long> trackIds) {
        Collection<TrackEntity> tracks = trackRepository.findAllById(trackIds);
        if (tracks.size() != trackIds.size()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Some tracks do not exist");
        }
        tracks.forEach(track -> track.removeLikedUser(user));
        trackRepository.saveAll(tracks);
    }
}
