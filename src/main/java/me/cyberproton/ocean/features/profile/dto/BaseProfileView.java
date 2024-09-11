package me.cyberproton.ocean.features.profile.dto;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.Mapping;

import me.cyberproton.ocean.features.profile.entity.ProfileEntity;

@EntityView(ProfileEntity.class)
public interface BaseProfileView {
    @IdMapping
    Long getId();

    @Mapping("user.username")
    String getUsername();

    String getName();

    String getBio();
}
