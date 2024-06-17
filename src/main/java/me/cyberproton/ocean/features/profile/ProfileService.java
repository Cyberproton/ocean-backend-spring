package me.cyberproton.ocean.features.profile;

import lombok.AllArgsConstructor;
import me.cyberproton.ocean.features.file.FileEntity;
import me.cyberproton.ocean.features.user.User;
import me.cyberproton.ocean.features.user.UserRepository;
import me.cyberproton.ocean.util.ImageUrlMapper;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ProfileService {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final ImageUrlMapper imageUrlMapper;

    public ProfileResponse getProfileByUserId(Long id) {
        return mapProfileToResponse(getOrCreateProfileByUserId(id));
    }

    public ProfileResponse updateProfileByUserId(Long id, UpdateProfileRequest request) {
        Profile profile = getOrCreateProfileByUserId(id);
        profile.setName(request.name());
        profile.setBio(request.bio());
        profile = profileRepository.save(profile);
        return mapProfileToResponse(profile);
    }

    public ProfileResponse updateProfileAvatarByUserId(Long id, FileEntity file) {
        Profile profile = getOrCreateProfileByUserId(id);
        profile.setAvatar(file);
        profile = profileRepository.save(profile);
        return mapProfileToResponse(profile);
    }

    public ProfileResponse updateProfileBannerByUserId(Long id, FileEntity file) {
        Profile profile = getOrCreateProfileByUserId(id);
        profile.setBanner(file);
        profile = profileRepository.save(profile);
        return mapProfileToResponse(profile);
    }

    private Profile getOrCreateProfileByUserId(Long id) {
        Profile profile = profileRepository.findByUserId(id).orElse(null);
        if (profile == null) {
            User user = userRepository.findById(id).orElseThrow();
            profile = Profile.builder().user(user).build();
            profile = profileRepository.save(profile);
        }
        return profile;
    }

    private ProfileResponse mapProfileToResponse(Profile profile) {
        return ProfileResponse.builder()
                              .id(profile.getId())
                              .username(profile.getUser().getUsername())
                              .name(profile.getName())
                              .bio(profile.getBio())
                              .avatarUrl(imageUrlMapper.mapFileToUrl(profile.getAvatar()))
                              .bannerUrl(imageUrlMapper.mapFileToUrl(profile.getBanner()))
                              .numberOfFollowers(userRepository.countByFollowersId(profile.getUser().getId()))
                              .numberOfFollowings(userRepository.countByFollowingId(profile.getUser().getId()))
                              .build();
    }
}
