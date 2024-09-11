package me.cyberproton.ocean.features.album.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import me.cyberproton.ocean.features.album.entity.AlbumType;
import me.cyberproton.ocean.features.album.util.AlbumConstant;

import java.util.Date;
import java.util.Set;

public record CreateOrUpdateAlbumRequest(
        AlbumType type,
        @NotBlank @Size(max = AlbumConstant.MAX_ALBUM_NAME_LENGTH) String name,
        @Nullable String description,
        Date releaseDate,
        Long recordLabelId,
        @NotEmpty Set<Long> copyrightIds) {}
