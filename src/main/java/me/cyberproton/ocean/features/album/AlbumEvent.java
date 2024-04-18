package me.cyberproton.ocean.features.album;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class AlbumEvent extends ApplicationEvent {
    private final Type type;
    private final Album album;

    public AlbumEvent(Type type, Album album) {
        super(album);
        this.type = type;
        this.album = album;
    }

    public enum Type {
        CREATE,
        UPDATE,
        DELETE
    }
}
