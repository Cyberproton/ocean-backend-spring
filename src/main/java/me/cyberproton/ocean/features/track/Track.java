package me.cyberproton.ocean.features.track;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.cyberproton.ocean.features.album.Album;
import me.cyberproton.ocean.features.artist.Artist;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class Track {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private Integer trackNumber;

    private Long duration;

    @ManyToOne(optional = false)
    private Album album;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "tracks_artists",
            joinColumns = @JoinColumn(
                    name = "track_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "artist_id", referencedColumnName = "id"))
    private Set<Artist> artists;

    public void addArtist(Artist artist) {
        artists.add(artist);
        artist.getTracks().add(this);
    }

    public void removeArtist(Artist artist) {
        artists.remove(artist);
        artist.getTracks().remove(this);
    }
}
