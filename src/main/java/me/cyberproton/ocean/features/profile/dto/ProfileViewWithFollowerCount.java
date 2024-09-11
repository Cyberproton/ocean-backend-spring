package me.cyberproton.ocean.features.profile.dto;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.Mapping;

import me.cyberproton.ocean.features.profile.entity.ProfileEntity;

@EntityView(ProfileEntity.class)
public interface ProfileViewWithFollowerCount extends ProfileViewWithAvatarAndBanner {
    @Mapping("size(user.followers)")
    Long getNumberOfFollowers();

    @Mapping("size(user.following)")
    Long getNumberOfFollowings();
}
