package me.cyberproton.ocean.features.playlist.event;

import lombok.Getter;

import org.springframework.context.ApplicationEvent;

import java.util.List;

@Getter
public class PlaylistPlayedEvent extends ApplicationEvent {
    private final List<PlayedPlaylist> playlists;

    public PlaylistPlayedEvent(List<PlayedPlaylist> source) {
        super(source);
        this.playlists = source;
    }

    public record PlayedPlaylist(long playlistId, long playedAt) {}
}
