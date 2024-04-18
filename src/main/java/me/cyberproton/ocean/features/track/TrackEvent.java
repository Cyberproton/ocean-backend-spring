package me.cyberproton.ocean.features.track;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class TrackEvent extends ApplicationEvent {
    private final Type type;
    private final Track track;

    public TrackEvent(Type type, Track track) {
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
