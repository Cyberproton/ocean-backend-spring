package me.cyberproton.ocean.features.album.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.FetchStrategy;
import com.blazebit.persistence.view.Mapping;

import me.cyberproton.ocean.features.album.entity.AlbumEntity;
import me.cyberproton.ocean.features.file.FileView;

import java.util.List;

@EntityView(AlbumEntity.class)
public interface AlbumViewWithCovers extends BaseAlbumView {
    @Mapping(fetch = FetchStrategy.MULTISET)
    List<FileView> getCovers();
}
