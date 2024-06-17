package me.cyberproton.ocean.features.album;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.cyberproton.ocean.features.file.FileDocument;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.List;

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

    @Field(type = FieldType.Nested)
    private List<FileDocument> covers;
}
