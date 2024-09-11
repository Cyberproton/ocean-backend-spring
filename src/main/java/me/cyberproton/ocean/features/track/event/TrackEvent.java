package me.cyberproton.ocean.features.track.event;

import lombok.Getter;

import me.cyberproton.ocean.features.track.entity.TrackEntity;

import org.springframework.context.ApplicationEvent;

@Getter
public class TrackEvent extends ApplicationEvent {
    private final Type type;
    private final TrackEntity track;

    public TrackEvent(Type type, TrackEntity track) {
        super(track);
        this.type = type;
        this.track = track;
    }

    public enum Type {
        CREATE,
        UPDATE,
        DELETE
    }
}
