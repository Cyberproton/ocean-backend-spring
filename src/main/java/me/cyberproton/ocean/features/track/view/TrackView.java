package me.cyberproton.ocean.features.track.view;

import com.blazebit.persistence.view.*;

import me.cyberproton.ocean.features.album.dto.BaseAlbumView;
import me.cyberproton.ocean.features.profile.dto.BaseProfileView;
import me.cyberproton.ocean.features.track.entity.TrackEntity;

import java.util.List;

@EntityView(TrackEntity.class)
public interface TrackView {
    @IdMapping
    Long getId();

    String getName();

    Integer getTrackNumber();

    Integer getDuration();

    BaseAlbumView getAlbum();

    @Mapping(value = "artists.user.profile", fetch = FetchStrategy.MULTISET)
    List<BaseProfileView> getArtists();
}
