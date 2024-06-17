package me.cyberproton.ocean.features.album;

import lombok.Builder;
import me.cyberproton.ocean.features.copyright.CopyrightResponse;
import me.cyberproton.ocean.features.file.ImageResponse;
import me.cyberproton.ocean.features.recordlabel.RecordLabelResponse;
import me.cyberproton.ocean.util.ImageUrlMapper;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Builder
public record AlbumResponse(
        Long id,
        AlbumType type,
        String name,
        String description,
        Date releaseDate,
        List<ImageResponse> covers,
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
                            .covers(album.getCovers().stream()
                                         .map(fileEntity -> ImageResponse.fromEntity(fileEntity, imageUrlMapper))
                                         .toList())
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
                            .covers(albumDocument.getCovers().stream()
                                                 .map(fileDocument -> ImageResponse.fromElasticsearchDocument(
                                                         fileDocument, imageUrlMapper))
                                                 .toList())
                            //.recordLabel(RecordLabelResponse.fromEntity(albumDocument.getRecordLabel()))
                            .build();
    }
}
