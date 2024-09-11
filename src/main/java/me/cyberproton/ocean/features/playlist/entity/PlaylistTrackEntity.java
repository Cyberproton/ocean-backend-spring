package me.cyberproton.ocean.features.playlist.entity;

import jakarta.persistence.*;

import lombok.*;

import me.cyberproton.ocean.features.track.entity.TrackEntity;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity(name = "playlist_track")
@EntityListeners(PlaylistTrackListener.class)
public class PlaylistTrackEntity {
    @EmbeddedId private PlaylistTrackKey id;

    @ManyToOne
    @MapsId("playlistId")
    @JoinColumn(name = "playlist_id")
    private PlaylistEntity playlist;

    @ManyToOne
    @MapsId("trackId")
    @JoinColumn(name = "track_id")
    private TrackEntity track;

    private Long trackPosition;
}
