package me.cyberproton.ocean.features.track.entity;

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
@Entity(name = "track_likes")
public class TrackLikeEntity {
    @EmbeddedId private TrackLikeEntityKey id;

    @ManyToOne
    @MapsId("trackId")
    @JoinColumn(name = "track_id")
    private TrackEntity track;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(nullable = false)
    @UpdateTimestamp
    private Date likedAt;
}
