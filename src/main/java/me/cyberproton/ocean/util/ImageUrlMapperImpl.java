package me.cyberproton.ocean.util;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import me.cyberproton.ocean.config.ExternalAppConfig;
import me.cyberproton.ocean.features.file.FileEntity;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class ImageUrlMapperImpl implements ImageUrlMapper {
    private final ExternalAppConfig externalAppConfig;

    @Nullable
    public String mapFileIdToUrl(@Nullable Long id) {
        if (id == null) {
            return null;
        }
        return externalAppConfig.domain() + "/api/v1/images/" + id;
    }

    @Nullable
    public String mapFileToUrl(@Nullable FileEntity file) {
        if (file == null) {
            return null;
        }
        return mapFileIdToUrl(file.getId());
    }
}
