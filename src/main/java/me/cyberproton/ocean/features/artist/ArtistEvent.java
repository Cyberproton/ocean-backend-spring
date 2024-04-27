package me.cyberproton.ocean.features.artist;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ArtistEvent extends ApplicationEvent {
    private final Type type;
    private final Artist artist;

    public ArtistEvent(Type type, Artist artist) {
        super(artist);
        this.type = type;
        this.artist = artist;
    }

    public enum Type {
        CREATE,
        UPDATE,
        DELETE
    }
}
