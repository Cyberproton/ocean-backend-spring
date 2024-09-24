package me.cyberproton.ocean.features.playlist.event;

import lombok.Getter;

import me.cyberproton.ocean.features.playlist.entity.PlaylistEntity;

import org.springframework.context.ApplicationEvent;

@Getter
public class PlaylistEvent extends ApplicationEvent {
    private final PlaylistEntity playlist;
    private final Type type;

    public PlaylistEvent(Type type, PlaylistEntity playlist) {
        super(playlist);
        this.playlist = playlist;
        this.type = type;
    }

    @Override
    public PlaylistEntity getSource() {
        return playlist;
    }

    public enum Type {
        CREATE,
        UPDATE,
        DELETE
    }
}
