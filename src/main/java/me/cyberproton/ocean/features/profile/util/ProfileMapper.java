package me.cyberproton.ocean.features.profile.util;

import lombok.AllArgsConstructor;

import me.cyberproton.ocean.features.file.FileMapper;
import me.cyberproton.ocean.features.profile.dto.BaseProfileView;
import me.cyberproton.ocean.features.profile.dto.ProfileResponse;
import me.cyberproton.ocean.features.profile.dto.ProfileViewWithAvatarAndBanner;
import me.cyberproton.ocean.features.profile.dto.ProfileViewWithFollowerCount;
import me.cyberproton.ocean.features.profile.entity.ProfileDocument;
import me.cyberproton.ocean.features.profile.entity.ProfileEntity;
import me.cyberproton.ocean.features.user.UserDocument;
import me.cyberproton.ocean.features.user.UserEntity;
import me.cyberproton.ocean.features.user.UserRepositoryForMapper;
import me.cyberproton.ocean.mapper.MapStructUtils;

import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class ProfileMapper {
    private FileMapper fileMapper;
    private UserRepositoryForMapper userRepositoryForMapper;

    public ProfileResponse entityToResponse(ProfileEntity profileEntity) {
        Long numberOfFollowers = null;
        Long numberOfFollowings = null;
        if (profileEntity.getUser() != null
                && MapStructUtils.isLazyInitialized(profileEntity.getUser())) {
            UserEntity user = profileEntity.getUser();
            numberOfFollowers = userRepositoryForMapper.countByFollowersId(user.getId());
        }
        if (profileEntity.getUser() != null
                && MapStructUtils.isLazyInitialized(profileEntity.getUser())) {
            numberOfFollowings =
                    userRepositoryForMapper.countByFollowingId(profileEntity.getUser().getId());
        }

        return ProfileResponse.builder()
                .id(profileEntity.getId())
                .username(profileEntity.getUser().getUsername())
                .name(profileEntity.getName())
                .bio(profileEntity.getBio())
                .avatars(fileMapper.singleToMultipleResponses(profileEntity.getAvatar()))
                .banners(fileMapper.singleToMultipleResponses(profileEntity.getBanner()))
                .numberOfFollowers(numberOfFollowers)
                .numberOfFollowings(numberOfFollowings)
                .build();
    }

    public ProfileResponse documentToResponse(UserDocument userDocument) {
        ProfileDocument profileDocument = userDocument.getProfile();

        return ProfileResponse.builder()
                .id(userDocument.getId())
                .username(userDocument.getUsername())
                .name(profileDocument.getName())
                .bio(profileDocument.getBio())
                .avatars(fileMapper.singleToMultipleResponses(profileDocument.getAvatar()))
                .banners(fileMapper.singleToMultipleResponses(profileDocument.getBanner()))
                .numberOfFollowers(userDocument.getNumberOfFollowers())
                .numberOfFollowings(userDocument.getNumberOfFollowing())
                .build();
    }

    public ProfileResponse viewToResponse(BaseProfileView profileView) {
        return ProfileResponse.builder()
                .id(profileView.getId())
                .username(profileView.getUsername())
                .name(profileView.getName())
                .bio(profileView.getBio())
                .build();
    }

    public ProfileResponse viewToResponse(ProfileViewWithAvatarAndBanner profileView) {
        return ProfileResponse.builder()
                .id(profileView.getId())
                .username(profileView.getUsername())
                .name(profileView.getName())
                .bio(profileView.getBio())
                .avatars(fileMapper.singleToMultipleResponses(profileView.getAvatar()))
                .banners(fileMapper.singleToMultipleResponses(profileView.getBanner()))
                .build();
    }

    public ProfileResponse viewToResponse(ProfileViewWithFollowerCount profileView) {
        return ProfileResponse.builder()
                .id(profileView.getId())
                .username(profileView.getUsername())
                .name(profileView.getName())
                .bio(profileView.getBio())
                .avatars(fileMapper.singleToMultipleResponses(profileView.getAvatar()))
                .banners(fileMapper.singleToMultipleResponses(profileView.getBanner()))
                .numberOfFollowers(profileView.getNumberOfFollowers())
                .numberOfFollowings(profileView.getNumberOfFollowings())
                .build();
    }
}
