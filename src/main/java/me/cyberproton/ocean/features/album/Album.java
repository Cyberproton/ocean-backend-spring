package me.cyberproton.ocean.features.album;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.cyberproton.ocean.features.copyright.Copyright;
import me.cyberproton.ocean.features.file.FileEntity;
import me.cyberproton.ocean.features.recordlabel.RecordLabel;
import me.cyberproton.ocean.features.track.Track;

import java.util.Date;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@EntityListeners(AlbumListener.class)
public class Album {
    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private AlbumType type;

    @Column(length = AlbumConstant.MAX_ALBUM_NAME_LENGTH)
    private String name;

    @Column(length = AlbumConstant.MAX_ALBUM_DESCRIPTION_LENGTH)
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

    @OneToMany
    private Set<Track> tracks;
}
