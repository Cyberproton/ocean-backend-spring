package me.cyberproton.ocean.features.playlist;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PlaylistEvent extends ApplicationEvent {
    private final Playlist playlist;
    private final Type type;

    public PlaylistEvent(Type type, Playlist playlist) {
        super(playlist);
        this.playlist = playlist;
        this.type = type;
    }

    @Override
    public Playlist getSource() {
        return playlist;
    }

    public enum Type {
        CREATE,
        UPDATE,
        DELETE
    }
}
