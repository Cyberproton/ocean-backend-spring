package me.cyberproton.ocean.features.playlist.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Embeddable
public class PlaylistTrackKey implements Serializable {
    @Column(name = "playlist_id")
    private Long playlistId;

    @Column(name = "track_id")
    private Long trackId;
}
