package me.cyberproton.ocean.util;

import jakarta.annotation.Nullable;
import me.cyberproton.ocean.features.file.FileEntity;

public interface ImageUrlMapper {
    @Nullable String mapFileToUrl(@Nullable FileEntity file);

    @Nullable String mapFileIdToUrl(@Nullable Long fileId);
}
