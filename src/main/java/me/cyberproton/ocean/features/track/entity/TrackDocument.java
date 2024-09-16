package me.cyberproton.ocean.features.track.entity;

import jakarta.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import me.cyberproton.ocean.features.file.FileDocument;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.ScriptedField;

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

    private Long albumId;

    private Long numberOfPlays;

    private Long numberOfLikes;

    @ScriptedField private Long popularity;
}
