package me.cyberproton.ocean.features.album.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;

import me.cyberproton.ocean.features.album.entity.AlbumEntity;
import me.cyberproton.ocean.features.album.entity.AlbumType;

import java.util.Date;

@EntityView(AlbumEntity.class)
public interface BaseAlbumView {
    @IdMapping
    Long getId();

    AlbumType getType();

    String getName();

    String getDescription();

    Date getReleaseDate();
}
