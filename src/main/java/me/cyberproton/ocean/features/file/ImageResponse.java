package me.cyberproton.ocean.features.file;

import lombok.Builder;
import me.cyberproton.ocean.util.ImageUrlMapper;

@Builder
public record ImageResponse(
        String url,
        double width,
        double height
) {
    public static ImageResponse fromEntity(
            FileEntity imageFile,
            ImageUrlMapper imageUrlMapper
    ) {
        return ImageResponse.builder()
                            .url(imageUrlMapper.mapFileToUrl(imageFile))
                            .width(imageFile.getWidth())
                            .height(imageFile.getHeight())
                            .build();
    }

    public static ImageResponse fromElasticsearchDocument(
            FileDocument imageFile,
            ImageUrlMapper imageUrlMapper
    ) {
        return ImageResponse.builder()
                            .url(imageUrlMapper.mapFileIdToUrl(imageFile.getId()))
                            .width(imageFile.getWidth())
                            .height(imageFile.getHeight())
                            .build();
    }
}
