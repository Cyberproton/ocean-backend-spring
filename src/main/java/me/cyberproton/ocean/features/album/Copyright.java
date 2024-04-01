package me.cyberproton.ocean.features.album;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private String type;

    @ManyToMany(mappedBy = "copyrights")
    private Set<Album> albums;
}
