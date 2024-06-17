package me.cyberproton.ocean.features.playlist;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.cyberproton.ocean.features.file.FileEntity;
import me.cyberproton.ocean.features.user.User;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@EntityListeners(PlaylistListener.class)
public class Playlist {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Column(length = PlaylistConstants.PLAYLIST_DESCRIPTION_MAX_LENGTH)
    private String description;

    private boolean isPublic;

    @OneToMany
    private List<FileEntity> covers;

    @OneToMany(mappedBy = "playlist", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<PlaylistTrack> playlistTracks;

    @ManyToOne
    private User owner;

    public void addAllPlaylistTracks(Set<PlaylistTrack> playlistTracks) {
        this.playlistTracks.addAll(playlistTracks);
        playlistTracks.forEach(playlistTrack -> playlistTrack.setPlaylist(this));
    }

    public void removeAllPlaylistTracks(Set<PlaylistTrack> playlistTracks) {
        this.playlistTracks.removeAll(playlistTracks);
        playlistTracks.forEach(playlistTrack -> playlistTrack.setPlaylist(null));
    }
}
