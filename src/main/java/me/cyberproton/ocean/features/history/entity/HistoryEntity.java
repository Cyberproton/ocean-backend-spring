package me.cyberproton.ocean.features.history.entity;

import jakarta.persistence.*;

import lombok.*;

import me.cyberproton.ocean.features.album.entity.AlbumEntity;
import me.cyberproton.ocean.features.artist.ArtistEntity;
import me.cyberproton.ocean.features.playlist.entity.PlaylistEntity;
import me.cyberproton.ocean.features.track.entity.TrackEntity;
import me.cyberproton.ocean.features.user.UserEntity;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Entity(name = "history")
public class HistoryEntity {
    @Id @GeneratedValue private Long id;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private HistoryType type;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

    @ManyToOne private TrackEntity track;

    @ManyToOne private AlbumEntity album;

    @ManyToOne private ArtistEntity artist;

    @ManyToOne private PlaylistEntity playlist;
}
