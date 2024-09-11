package me.cyberproton.ocean.features.album.event;

import lombok.Getter;

import me.cyberproton.ocean.features.album.entity.AlbumEntity;

import org.springframework.context.ApplicationEvent;

@Getter
public class AlbumEvent extends ApplicationEvent {
    private final Type type;
    private final AlbumEntity album;

    public AlbumEvent(Type type, AlbumEntity album) {
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
