package me.cyberproton.ocean.features.artist;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.cyberproton.ocean.features.track.Track;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class Artist {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToMany(mappedBy = "artists", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Track> tracks;

    public void addTrack(Track track) {
        tracks.add(track);
        track.getArtists().add(this);
    }

    public void removeTrack(Track track) {
        tracks.remove(track);
        track.getArtists().remove(this);
    }
}
