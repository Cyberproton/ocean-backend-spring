package me.cyberproton.ocean.features.copyright;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import me.cyberproton.ocean.features.album.entity.AlbumEntity;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity(name = "copyright")
public class CopyrightEntity {
    @Id @GeneratedValue private Long id;

    private String text;

    @Enumerated(EnumType.STRING)
    private CopyrightType type;

    @ManyToMany(mappedBy = "copyrights")
    private Set<AlbumEntity> albums;
}
