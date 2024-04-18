package me.cyberproton.ocean.features.playlist;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

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

    private Long coverId;

    private Long ownerId;
}
