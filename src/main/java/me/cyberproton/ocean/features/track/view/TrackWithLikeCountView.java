package me.cyberproton.ocean.features.track.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.Mapping;
import com.blazebit.persistence.view.UpdatableEntityView;

import me.cyberproton.ocean.features.track.entity.TrackEntity;
import me.cyberproton.ocean.features.user.UserEntity;

import java.util.Set;

@UpdatableEntityView
@EntityView(TrackEntity.class)
public interface TrackWithLikeCountView {
    @IdMapping
    Long getId();

    @Mapping("size(likedUsers)")
    Long getNumberOfLikes();

    Set<UserEntity> getLikedUsers();

    void setLikedUsers(Set<UserEntity> likedUsers);
}
