package me.cyberproton.ocean.features.artist;

import jakarta.persistence.*;

import lombok.*;

import me.cyberproton.ocean.features.track.entity.TrackEntity;
import me.cyberproton.ocean.features.user.UserEntity;

import org.hibernate.annotations.BatchSize;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity(name = "artist")
@EntityListeners(ArtistListener.class)
@NamedEntityGraph(name = "eager-artist", attributeNodes = @NamedAttributeNode(value = "user"))
public class ArtistEntity {
    @Id @GeneratedValue private Long id;

    @OneToOne private UserEntity user;

    @BatchSize(size = 20)
    @ManyToMany(mappedBy = "artists", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<TrackEntity> tracks;

    public void addTrack(TrackEntity track) {
        tracks.add(track);
        track.getArtists().add(this);
    }

    public void removeTrack(TrackEntity track) {
        tracks.remove(track);
        track.getArtists().remove(this);
    }
}
