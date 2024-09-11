package me.cyberproton.ocean.features.profile.entity;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;

import lombok.RequiredArgsConstructor;

import me.cyberproton.ocean.features.user.UserElasticRepository;
import me.cyberproton.ocean.features.user.UserEntity;

@RequiredArgsConstructor
public class ProfileListener {
    private final UserElasticRepository userElasticRepository;

    @PostPersist
    @PostUpdate
    public void createProfileOnUserCreate(ProfileEntity profileEntity) {
        UserEntity user = profileEntity.getUser();
        user.setProfile(profileEntity);
        userElasticRepository.save(user);
    }
}
