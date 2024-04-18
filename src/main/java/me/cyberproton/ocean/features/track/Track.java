package me.cyberproton.ocean.features.track;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.cyberproton.ocean.features.album.Album;
import me.cyberproton.ocean.features.artist.Artist;
import me.cyberproton.ocean.features.file.FileEntity;
import me.cyberproton.ocean.features.genre.Genre;
import me.cyberproton.ocean.features.playlist.PlaylistTrack;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@EntityListeners(TrackListener.class)
public class Track {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private Integer trackNumber;

    private Long duration;

    @OneToOne
    private FileEntity file;

    @ManyToMany
    @JoinTable(
            name = "track_genres",
            joinColumns = @JoinColumn(
                    name = "track_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "genre_id", referencedColumnName = "id"))
    private Set<Genre> genres;

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

    @OneToMany(mappedBy = "track", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<PlaylistTrack> playlistTracks;

    public void addArtist(Artist artist) {
        artists.add(artist);
        artist.getTracks().add(this);
    }

    public void removeArtist(Artist artist) {
        artists.remove(artist);
        artist.getTracks().remove(this);
    }
}
