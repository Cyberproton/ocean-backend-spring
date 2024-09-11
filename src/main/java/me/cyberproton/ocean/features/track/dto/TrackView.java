package me.cyberproton.ocean.features.track.dto;

import com.blazebit.persistence.SubqueryInitiator;
import com.blazebit.persistence.view.*;

import me.cyberproton.ocean.features.album.dto.BaseAlbumView;
import me.cyberproton.ocean.features.analytics.TrackAnalyticsEntity;
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

    @MappingSubquery(TrackPlayCountSubqueryProvider.class)
    TrackAnalyticsEntity getAnalytics();

    class TrackPlayCountSubqueryProvider implements SubqueryProvider {
        @Override
        public <T> T createSubquery(SubqueryInitiator<T> subqueryInitiator) {
            return subqueryInitiator
                    .from(TrackAnalyticsEntity.class, "tae")
                    .select("numberOfPlays")
                    .where("tae.track.id")
                    .eqExpression("id")
                    .end();
        }
    }
}
