package me.cyberproton.ocean.features.playlist.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.springframework.context.ApplicationEvent;

import java.util.List;

@Getter
public class PlaylistSavedEvent extends ApplicationEvent {
    private final List<SavedPlaylist> playlists;
    private final Type type;

    public PlaylistSavedEvent(List<SavedPlaylist> source, Type type) {
        super(source);
        this.playlists = source;
        this.type = type;
    }

    public enum Type {
        SAVED,
        UNSAVED
    }

    @AllArgsConstructor
    @Getter
    public static class SavedPlaylist {
        private final long playlistId;
        private final Type type;
        private final long savedAt;
    }
}
