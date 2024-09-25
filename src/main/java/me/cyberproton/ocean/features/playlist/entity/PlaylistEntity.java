package me.cyberproton.ocean.features.playlist.entity;

import jakarta.persistence.*;

import lombok.*;

import me.cyberproton.ocean.features.file.FileEntity;
import me.cyberproton.ocean.features.playlist.util.PlaylistConstants;
import me.cyberproton.ocean.features.user.UserEntity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity(name = "playlist")
@EntityListeners(PlaylistListener.class)
public class PlaylistEntity {
    @Id @GeneratedValue private Long id;

    private String name;

    @Column(length = PlaylistConstants.PLAYLIST_DESCRIPTION_MAX_LENGTH)
    private String description;

    private boolean isPublic;

    @OneToMany private List<FileEntity> covers;

    @OneToMany(mappedBy = "playlist", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<PlaylistTrackEntity> playlistTracks;

    @ManyToOne private UserEntity owner;

    @ManyToMany
    @JoinTable(
            name = "user_saved_playlist",
            joinColumns = @JoinColumn(name = "playlist_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private Set<UserEntity> savedUsers;

    @OneToOne(mappedBy = "playlist", fetch = FetchType.LAZY)
    private PlaylistAnalyticsEntity analytics;

    public void addAllPlaylistTracks(Set<PlaylistTrackEntity> playlistTracks) {
        if (this.playlistTracks == null) {
            setPlaylistTracks(new HashSet<>());
        }
        this.playlistTracks.addAll(playlistTracks);
        playlistTracks.forEach(playlistTrack -> playlistTrack.setPlaylist(this));
    }

    public void removeAllPlaylistTracks(Set<PlaylistTrackEntity> playlistTracks) {
        if (this.playlistTracks == null) {
            return;
        }
        this.playlistTracks.removeAll(playlistTracks);
        playlistTracks.forEach(playlistTrack -> playlistTrack.setPlaylist(null));
    }

    public void addSavedUser(UserEntity user) {
        if (this.savedUsers == null) {
            setSavedUsers(new HashSet<>());
        }
        this.savedUsers.add(user);
    }

    public void removeSavedUser(UserEntity user) {
        if (this.savedUsers == null) {
            return;
        }
        this.savedUsers.remove(user);
    }
}
