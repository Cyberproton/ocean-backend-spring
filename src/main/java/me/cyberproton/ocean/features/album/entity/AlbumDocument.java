package me.cyberproton.ocean.features.album.entity;

import jakarta.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import me.cyberproton.ocean.features.copyright.CopyrightDocument;
import me.cyberproton.ocean.features.file.FileDocument;
import me.cyberproton.ocean.features.recordlabel.RecordLabelDocument;
import me.cyberproton.ocean.features.user.UserDocument;

import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Document(indexName = "album")
public class AlbumDocument {
    @Id private Long id;

    private AlbumType type;

    private String name;

    private String description;

    private Date releaseDate;

    private List<CopyrightDocument> copyrights;

    private RecordLabelDocument recordLabel;

    private List<FileDocument> covers;

    private List<UserDocument> artists;
}
