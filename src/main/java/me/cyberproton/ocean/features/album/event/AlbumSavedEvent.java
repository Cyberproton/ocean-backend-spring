package me.cyberproton.ocean.features.album.event;

import lombok.Getter;

import org.springframework.context.ApplicationEvent;

import java.util.List;

@Getter
public class AlbumSavedEvent extends ApplicationEvent {
    private final List<SavedAlbum> albums;

    public AlbumSavedEvent(List<SavedAlbum> source) {
        super(source);
        this.albums = source;
    }

    public enum Type {
        SAVED,
        UNSAVED
    }

    public record SavedAlbum(long albumId, long savedAt) {}
}
