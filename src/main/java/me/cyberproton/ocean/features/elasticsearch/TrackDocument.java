package me.cyberproton.ocean.features.elasticsearch;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Document(indexName = "track")
public class TrackDocument {
    @Id
    private Long id;

    @Field
    private String name;

    @Field
    private Integer trackNumber;

    @Field
    private Long duration;

    @Field
    private Long fileId;

    @Field
    private Long albumId;
}
