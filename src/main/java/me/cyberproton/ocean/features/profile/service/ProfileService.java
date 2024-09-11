package me.cyberproton.ocean.features.profile.service;

import lombok.AllArgsConstructor;

import me.cyberproton.ocean.features.file.FileEntity;
import me.cyberproton.ocean.features.profile.dto.ProfileResponse;
import me.cyberproton.ocean.features.profile.dto.UpdateProfileRequest;
import me.cyberproton.ocean.features.profile.entity.ProfileEntity;
import me.cyberproton.ocean.features.profile.repository.ProfileRepository;
import me.cyberproton.ocean.features.profile.util.ProfileMapper;
import me.cyberproton.ocean.features.user.UserEntity;
import me.cyberproton.ocean.features.user.UserRepository;

import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ProfileService {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;

    public ProfileResponse getProfileByUserId(Long id) {
        return mapProfileToResponse(getOrCreateProfileByUserId(id));
    }

    public ProfileResponse updateProfileByUserId(Long id, UpdateProfileRequest request) {
        ProfileEntity profile = getOrCreateProfileByUserId(id);
        profile.setName(request.name());
        profile.setBio(request.bio());
        profile = profileRepository.save(profile);
        return mapProfileToResponse(profile);
    }

    public ProfileResponse updateProfileAvatarByUserId(Long id, FileEntity file) {
        ProfileEntity profile = getOrCreateProfileByUserId(id);
        profile.setAvatar(file);
        profile = profileRepository.save(profile);
        return mapProfileToResponse(profile);
    }

    public ProfileResponse updateProfileBannerByUserId(Long id, FileEntity file) {
        ProfileEntity profile = getOrCreateProfileByUserId(id);
        profile.setBanner(file);
        profile = profileRepository.save(profile);
        return mapProfileToResponse(profile);
    }

    private ProfileEntity getOrCreateProfileByUserId(Long id) {
        ProfileEntity profile = profileRepository.findByUserId(id).orElse(null);
        if (profile == null) {
            profile = createProfileByUserId(id);
        }
        return profile;
    }

    public ProfileEntity createProfileByUserId(Long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow();
        ProfileEntity profile = ProfileEntity.builder().user(user).build();
        profile = profileRepository.save(profile);
        return profile;
    }

    private ProfileResponse mapProfileToResponse(ProfileEntity profile) {
        return profileMapper.entityToResponse(profile);
    }
}
