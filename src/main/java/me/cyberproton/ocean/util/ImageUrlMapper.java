package me.cyberproton.ocean.util;

import jakarta.annotation.Nullable;
import me.cyberproton.ocean.features.file.FileEntity;

@FunctionalInterface
public interface ImageUrlMapper {
    @Nullable
    String mapFileToUrl(@Nullable FileEntity file);
}
