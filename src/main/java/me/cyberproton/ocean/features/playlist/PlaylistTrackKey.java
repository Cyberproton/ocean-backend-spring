package me.cyberproton.ocean.features.playlist;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

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
