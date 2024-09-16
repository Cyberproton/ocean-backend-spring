package me.cyberproton.ocean.features.track.event;

import lombok.Getter;

import me.cyberproton.ocean.features.track.dto.TrackLike;
import me.cyberproton.ocean.features.user.UserEntity;

import org.springframework.context.ApplicationEvent;

import java.util.List;

@Getter
public class TracksLikeChangeEvent extends ApplicationEvent {
    private final List<TrackLike> trackLikes;
    private final UserEntity user;
    private final Type type;

    public TracksLikeChangeEvent(List<TrackLike> trackLikes, UserEntity user, Type type) {
        super(trackLikes);
        this.trackLikes = trackLikes;
        this.user = user;
        this.type = type;
    }

    public enum Type {
        LIKE,
        UNLIKE
    }
}
