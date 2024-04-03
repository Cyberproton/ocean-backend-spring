package me.cyberproton.ocean.features.recordlabel;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
public class RecordLabel {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @OneToMany
    private Set<Album> albums;
}
