package me.cyberproton.ocean.features.genre;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import me.cyberproton.ocean.features.track.entity.TrackEntity;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity(name = "genre")
public class GenreEntity {
    @Id @GeneratedValue private Long id;

    @Column(unique = true)
    private String name;

    @ManyToMany(mappedBy = "genres")
    private Set<TrackEntity> tracks;
}
