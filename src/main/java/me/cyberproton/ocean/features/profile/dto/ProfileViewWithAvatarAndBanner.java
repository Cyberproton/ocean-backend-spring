package me.cyberproton.ocean.features.profile.dto;

import com.blazebit.persistence.view.EntityView;

import me.cyberproton.ocean.features.file.FileView;
import me.cyberproton.ocean.features.profile.entity.ProfileEntity;

@EntityView(ProfileEntity.class)
public interface ProfileViewWithAvatarAndBanner extends BaseProfileView {
    FileView getAvatar();

    FileView getBanner();
}
