package me.cyberproton.ocean.features.playlist.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.FetchStrategy;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.Mapping;

import me.cyberproton.ocean.features.file.FileView;
import me.cyberproton.ocean.features.playlist.entity.PlaylistEntity;
import me.cyberproton.ocean.features.profile.dto.ProfileViewWithAvatarAndBanner;

import java.util.List;

@EntityView(PlaylistEntity.class)
public interface PlaylistView {
    @IdMapping
    Long getId();

    String getName();

    String getDescription();

    boolean getIsPublic();

    @Mapping(fetch = FetchStrategy.MULTISET)
    List<FileView> getCovers();

    @Mapping(value = "owner.profile")
    ProfileViewWithAvatarAndBanner getOwner();

    @Mapping("size(playlistTracks)")
    Long getTotalTracks();

    @Mapping("analytics.popularity")
    Long getPopularity();
}
