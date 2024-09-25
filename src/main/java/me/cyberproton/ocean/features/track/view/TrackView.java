package me.cyberproton.ocean.features.track.view;

import com.blazebit.persistence.view.*;

import me.cyberproton.ocean.features.album.view.AlbumViewWithCovers;
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

    AlbumViewWithCovers getAlbum();

    @Mapping(value = "artists.user.profile", fetch = FetchStrategy.MULTISET)
    List<BaseProfileView> getArtists();

    @Mapping(value = "analytics.numberOfPlays")
    Long getNumberOfPlays();

    @Mapping("analytics.popularity")
    Long getPopularity();
}
