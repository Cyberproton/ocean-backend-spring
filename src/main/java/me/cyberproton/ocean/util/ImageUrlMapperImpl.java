package me.cyberproton.ocean.util;

import jakarta.annotation.Nullable;
import me.cyberproton.ocean.config.ExternalAppConfig;
import me.cyberproton.ocean.features.file.FileEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class ImageUrlMapperImpl implements ImageUrlMapper {
    private final UriComponentsBuilder builder;

    public ImageUrlMapperImpl(ExternalAppConfig externalAppConfig) {
        this.builder =
                UriComponentsBuilder.fromHttpUrl(externalAppConfig.domain())
                        .pathSegment(externalAppConfig.apiV1Path())
                        .pathSegment("images");
    }

    @Nullable public String mapFileIdToUrl(@Nullable Long id) {
        if (id == null) {
            return null;
        }

        return builder.cloneBuilder().pathSegment(id.toString()).build().toUriString();
    }

    @Nullable public String mapFileToUrl(@Nullable FileEntity file) {
        if (file == null) {
            return null;
        }
        return mapFileIdToUrl(file.getId());
    }
}
