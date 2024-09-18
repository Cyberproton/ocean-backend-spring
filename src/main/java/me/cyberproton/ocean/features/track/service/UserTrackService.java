package me.cyberproton.ocean.features.track.service;

import lombok.AllArgsConstructor;

import me.cyberproton.ocean.domain.BaseQuery;
import me.cyberproton.ocean.features.track.dto.TrackLikeDto;
import me.cyberproton.ocean.features.track.dto.TrackResponse;
import me.cyberproton.ocean.features.track.entity.TrackEntity;
import me.cyberproton.ocean.features.track.entity.TrackLikeEntity;
import me.cyberproton.ocean.features.track.entity.TrackLikeEntityKey;
import me.cyberproton.ocean.features.track.event.TracksLikeChangeEvent;
import me.cyberproton.ocean.features.track.repository.TrackLikeRepository;
import me.cyberproton.ocean.features.track.repository.TrackRepository;
import me.cyberproton.ocean.features.track.repository.TrackViewRepository;
import me.cyberproton.ocean.features.track.util.TrackMapper;
import me.cyberproton.ocean.features.user.UserEntity;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service
public class UserTrackService {
    private final TrackRepository trackRepository;
    private final TrackLikeRepository trackLikeRepository;
    private final TrackViewRepository trackViewRepository;
    private final TrackMapper trackMapper;
    private final ApplicationEventPublisher eventPublisher;

    public List<TrackResponse> getLikedTracks(UserEntity user, BaseQuery query) {
        return trackViewRepository.findAllByLikedUsersContains(user, query).stream()
                .map(trackMapper::viewToResponse)
                .toList();
    }

    public void likeTracks(UserEntity user, Set<Long> trackIds) {
        Collection<TrackEntity> tracks = trackRepository.findAllById(trackIds.stream().toList());
        if (tracks.size() != trackIds.size()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Some tracks do not exist");
        }
        List<TrackLikeDto> trackLikeDtos = new ArrayList<>();
        for (TrackEntity track : tracks) {
            if (track.getLikedUsers().contains(user)) {
                continue;
            }
            track.getLikedUsers().add(user);
            trackLikeDtos.add(
                    new TrackLikeDto(
                            track.getId(),
                            (long) track.getLikedUsers().size(),
                            user.getId(),
                            System.currentTimeMillis(),
                            TrackLikeDto.LikeType.LIKE));
        }
        trackRepository.saveAll(tracks);
        if (!trackLikeDtos.isEmpty()) {
            eventPublisher.publishEvent(new TracksLikeChangeEvent(trackLikeDtos, user));
        }
    }

    public void unlikeTracks(UserEntity user, Set<Long> trackIds) {
        List<TrackLikeEntityKey> trackLikeEntityKeys =
                trackIds.stream()
                        .map(trackId -> new TrackLikeEntityKey(user.getId(), trackId))
                        .toList();
        List<TrackLikeEntity> trackLikes = trackLikeRepository.findAllById(trackLikeEntityKeys);
        //        if (trackLikes.size() != trackIds.size()) {
        //            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Some tracks do not
        // exist");
        //        }
        List<Long> trackLikesCount =
                trackLikes.stream()
                        .map(t -> trackLikeRepository.countAllByTrackId(t.getId().getTrackId()))
                        .toList();

        trackLikeRepository.deleteAll(trackLikes);

        List<TrackLikeDto> trackLikeDtos = new ArrayList<>();
        for (int i = 0; i < trackLikes.size(); i++) {
            trackLikeDtos.add(
                    new TrackLikeDto(
                            trackLikes.get(i).getId().getTrackId(),
                            trackLikesCount.get(i),
                            user.getId(),
                            trackLikes.get(i).getLikedAt().getTime(),
                            TrackLikeDto.LikeType.UNLIKE));
        }

        eventPublisher.publishEvent(new TracksLikeChangeEvent(trackLikeDtos, user));
    }
}
