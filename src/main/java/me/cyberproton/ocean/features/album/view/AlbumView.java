package me.cyberproton.ocean.features.album.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.FetchStrategy;
import com.blazebit.persistence.view.Mapping;

import me.cyberproton.ocean.features.album.entity.AlbumEntity;
import me.cyberproton.ocean.features.copyright.CopyrightView;
import me.cyberproton.ocean.features.file.FileView;
import me.cyberproton.ocean.features.profile.dto.ProfileViewWithAvatarAndBanner;
import me.cyberproton.ocean.features.recordlabel.RecordLabelView;

import java.util.List;

@EntityView(AlbumEntity.class)
public interface AlbumView extends BaseAlbumView {
    @Mapping(fetch = FetchStrategy.MULTISET)
    List<FileView> getCovers();

    RecordLabelView getRecordLabel();

    @Mapping(fetch = FetchStrategy.MULTISET)
    List<CopyrightView> getCopyrights();

    @Mapping(value = "tracks.artists.user.profile", fetch = FetchStrategy.MULTISET)
    List<ProfileViewWithAvatarAndBanner> getArtists();

    @Mapping(value = "analytics.popularity")
    Long getPopularity();
}
