package me.cyberproton.ocean.features.playlist.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import me.cyberproton.ocean.features.user.UserEntity;

import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity(name = "user_saved_playlist")
public class SavedPlaylistEntity {
    @EmbeddedId private SavedPlaylistEntityKey id;

    @ManyToOne
    @MapsId("playlistId")
    @JoinColumn(name = "playlist_id")
    private PlaylistEntity playlist;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(nullable = false)
    @UpdateTimestamp
    private Date savedAt;
}
