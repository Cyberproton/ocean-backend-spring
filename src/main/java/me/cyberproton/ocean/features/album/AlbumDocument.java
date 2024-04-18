package me.cyberproton.ocean.features.album;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Document(indexName = "album")
public class AlbumDocument {
    @Id
    private Long id;

    private AlbumType type;

    private String name;

    private String description;

    private Date releaseDate;

    private Long coverId;
}
