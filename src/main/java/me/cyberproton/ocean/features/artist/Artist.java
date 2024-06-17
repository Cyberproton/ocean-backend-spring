package me.cyberproton.ocean.features.artist;

import jakarta.persistence.*;
import lombok.*;
import me.cyberproton.ocean.features.track.Track;
import me.cyberproton.ocean.features.user.User;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@EntityListeners(ArtistListener.class)
public class Artist {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private User user;

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
