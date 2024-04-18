package me.cyberproton.ocean.features.album;

import lombok.Builder;
import me.cyberproton.ocean.features.copyright.CopyrightResponse;
import me.cyberproton.ocean.features.file.ImageResponse;
import me.cyberproton.ocean.features.recordlabel.RecordLabelResponse;
import me.cyberproton.ocean.util.ImageUrlMapper;

import java.util.Date;
import java.util.Set;

@Builder
public record AlbumResponse(
        Long id,
        AlbumType type,
        String name,
        String description,
        Date releaseDate,
        ImageResponse cover,
        RecordLabelResponse recordLabel,
        Set<CopyrightResponse> copyrights
) {
    public static AlbumResponse fromEntity(Album album, ImageUrlMapper imageUrlMapper) {
        return AlbumResponse.builder()
                .id(album.getId())
                .type(album.getType())
                .name(album.getName())
                .description(album.getDescription())
                .releaseDate(album.getReleaseDate())
                .cover(ImageResponse.fromEntity(album.getCover(), imageUrlMapper))
                .recordLabel(RecordLabelResponse.fromEntity(album.getRecordLabel()))
                .build();
    }

    public static AlbumResponse fromElasticsearchDocument(AlbumDocument albumDocument, ImageUrlMapper imageUrlMapper) {
        return AlbumResponse.builder()
                .id(albumDocument.getId())
                .type(albumDocument.getType())
                .name(albumDocument.getName())
                .description(albumDocument.getDescription())
                .releaseDate(albumDocument.getReleaseDate())
                .cover(ImageResponse.fromFileId(albumDocument.getCoverId(), imageUrlMapper))
                //.recordLabel(RecordLabelResponse.fromEntity(albumDocument.getRecordLabel()))
                .build();
    }
}
