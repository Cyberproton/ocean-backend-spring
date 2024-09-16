package me.cyberproton.ocean.features.track.repository;

import com.blazebit.persistence.PagedList;

import me.cyberproton.ocean.domain.BaseQuery;
import me.cyberproton.ocean.features.track.view.TrackView;
import me.cyberproton.ocean.features.user.UserEntity;

public interface TrackViewRepositoryExtension {
    PagedList<TrackView> findAllByLikedUsersContains(UserEntity user, BaseQuery query);
}
