package me.cyberproton.ocean.features.album.entity;

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
@Entity(name = "user_saved_album")
public class SavedAlbumEntity {
    @EmbeddedId private SavedAlbumEntityKey id;

    @ManyToOne
    @MapsId("albumId")
    @JoinColumn(name = "album_id")
    private AlbumEntity album;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(nullable = false)
    @UpdateTimestamp
    private Date savedAt;
}
