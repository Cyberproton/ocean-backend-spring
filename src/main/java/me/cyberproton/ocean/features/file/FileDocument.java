package me.cyberproton.ocean.features.file;

import jakarta.persistence.Enumerated;
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
@Document(indexName = "image")
public class FileDocument {
    @Id private Long id;

    @Builder.Default @Enumerated private FileType type = FileType.OTHER;

    private String name;

    private String mimetype;

    private String path;

    private Long size;

    private boolean isPublic;

    // Image specific fields

    private Integer width;

    private Integer height;
}
