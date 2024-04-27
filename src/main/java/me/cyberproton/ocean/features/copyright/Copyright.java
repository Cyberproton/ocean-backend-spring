package me.cyberproton.ocean.features.copyright;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.cyberproton.ocean.features.album.Album;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class Copyright {
    @Id
    @GeneratedValue
    private Long id;

    private String text;

    @Enumerated(EnumType.STRING)
    private CopyrightType type;

    @ManyToMany(mappedBy = "copyrights")
    private Set<Album> albums;
}
