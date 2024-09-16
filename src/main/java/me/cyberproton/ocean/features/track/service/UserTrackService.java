package me.cyberproton.ocean.features.track.service;

import lombok.AllArgsConstructor;

import me.cyberproton.ocean.domain.BaseQuery;
import me.cyberproton.ocean.features.track.dto.TrackLike;
import me.cyberproton.ocean.features.track.dto.TrackResponse;
import me.cyberproton.ocean.features.track.entity.TrackEntity;
import me.cyberproton.ocean.features.track.event.TracksLikeChangeEvent;
import me.cyberproton.ocean.features.track.repository.TrackRepository;
import me.cyberproton.ocean.features.track.repository.TrackViewRepository;
import me.cyberproton.ocean.features.track.util.TrackMapper;
import me.cyberproton.ocean.features.user.UserEntity;

import org.springframework.context.ApplicationEventPublisher;
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
    private final ApplicationEventPublisher eventPublisher;

    public List<TrackResponse> getLikedTracks(UserEntity user, BaseQuery query) {
        return trackViewRepository.findAllByLikedUsersContains(user, query).stream()
                .map(trackMapper::viewToResponse)
                .toList();
    }

    public void likeTracks(UserEntity user, Collection<Long> trackIds) {
        Collection<TrackEntity> tracks = trackRepository.findAllById(trackIds.stream().toList());
        if (tracks.size() != trackIds.size()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Some tracks do not exist");
        }
        tracks.forEach(track -> track.getLikedUsers().add(user));
        trackRepository.saveAll(tracks);
        List<TrackLike> trackLikes =
                tracks.stream()
                        .map(t -> new TrackLike(t.getId(), (long) t.getLikedUsers().size()))
                        .toList();
        eventPublisher.publishEvent(
                new TracksLikeChangeEvent(trackLikes, user, TracksLikeChangeEvent.Type.LIKE));
    }

    public void unlikeTracks(UserEntity user, Collection<Long> trackIds) {
        Collection<TrackEntity> tracks = trackRepository.findAllById(trackIds);
        if (tracks.size() != trackIds.size()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Some tracks do not exist");
        }
        tracks.forEach(track -> track.removeLikedUser(user));
        trackRepository.saveAll(tracks);
        List<TrackLike> trackLikes =
                tracks.stream()
                        .map(t -> new TrackLike(t.getId(), (long) t.getLikedUsers().size()))
                        .toList();
        eventPublisher.publishEvent(
                new TracksLikeChangeEvent(trackLikes, user, TracksLikeChangeEvent.Type.UNLIKE));
    }
}
