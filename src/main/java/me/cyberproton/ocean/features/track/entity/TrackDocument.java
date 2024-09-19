package me.cyberproton.ocean.features.track.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import me.cyberproton.ocean.features.file.FileDocument;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Document(indexName = "track")
public class TrackDocument {
    @Id private Long id;

    private String name;

    private Integer trackNumber;

    private Integer duration;

    private FileDocument file;

    @Field private Long albumId;

    @Nullable private Long numberOfPlays;

    @Nullable private Long playTotalTimestampInMinutes;

    @Nullable private Long numberOfLikes;

    @Nullable private Long likeTotalTimestampInMinutes;

    @Nullable private Long popularity;
}
