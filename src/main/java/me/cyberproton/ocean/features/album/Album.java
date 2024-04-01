package me.cyberproton.ocean.features.album;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.cyberproton.ocean.features.file.FileEntity;

import java.util.Date;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class Album {
    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private AlbumType type;

    private String name;

    private String description;

    private Date releaseDate;

    @OneToOne
    private FileEntity cover;

    @ManyToMany
    @JoinTable(
            name = "album_copyrights",
            joinColumns = @JoinColumn(
                    name = "album_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "copy_id", referencedColumnName = "id"))
    private Set<Copyright> copyrights;

    @ManyToOne
    private RecordLabel recordLabel;
}
