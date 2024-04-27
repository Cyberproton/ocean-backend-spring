package me.cyberproton.ocean.features.file;

import lombok.Builder;
import me.cyberproton.ocean.util.ImageUrlMapper;

@Builder
public record ImageResponse(
        String url
) {
    public static ImageResponse fromEntity(
            FileEntity imageFile,
            ImageUrlMapper imageUrlMapper
    ) {
        return ImageResponse.builder()
                .url(imageUrlMapper.mapFileToUrl(imageFile))
                .build();
    }

    public static ImageResponse fromFileId(
            Long fileId,
            ImageUrlMapper imageUrlMapper
    ) {
        return ImageResponse.builder()
                .url(imageUrlMapper.mapFileIdToUrl(fileId))
                .build();
    }
}
