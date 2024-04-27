package me.cyberproton.ocean.features.playlist;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.cyberproton.ocean.features.track.Track;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class PlaylistTrack {
    @EmbeddedId
    private PlaylistTrackKey id;

    @ManyToOne
    @MapsId("playlistId")
    @JoinColumn(name = "playlist_id")
    private Playlist playlist;

    @ManyToOne
    @MapsId("trackId")
    @JoinColumn(name = "track_id")
    private Track track;

    private Long trackPosition;
}
