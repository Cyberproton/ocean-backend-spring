package me.cyberproton.ocean.features.playlist;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.cyberproton.ocean.features.file.FileDocument;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Document(indexName = "playlist")
public class PlaylistDocument {
    @Id
    private Long id;

    private String name;

    private String description;

    private boolean isPublic;

    @Field(type = FieldType.Nested)
    private List<FileDocument> covers;

    private Long ownerId;
}
