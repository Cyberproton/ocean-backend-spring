package me.cyberproton.ocean.features.track.event;

import lombok.Getter;

import me.cyberproton.ocean.features.track.dto.TrackLikeDto;
import me.cyberproton.ocean.features.user.UserEntity;

import org.springframework.context.ApplicationEvent;

import java.util.List;

@Getter
public class TracksLikeChangeEvent extends ApplicationEvent {
    private final List<TrackLikeDto> trackLikeDtos;
    private final UserEntity user;

    public TracksLikeChangeEvent(List<TrackLikeDto> trackLikeDtos, UserEntity user) {
        super(trackLikeDtos);
        this.trackLikeDtos = trackLikeDtos;
        this.user = user;
    }
}
