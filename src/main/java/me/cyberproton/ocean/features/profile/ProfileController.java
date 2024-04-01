package me.cyberproton.ocean.features.profile;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import me.cyberproton.ocean.annotations.V1ApiRestController;
import me.cyberproton.ocean.config.AppUserDetails;
import me.cyberproton.ocean.features.file.FileEntity;
import me.cyberproton.ocean.features.file.FileService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@V1ApiRestController
@RequestMapping("/profiles")
public class ProfileController {
    private final ProfileService profileService;
    private final FileService fileService;

    @GetMapping("/me")
    public ProfileResponse getMyProfile(
            @AuthenticationPrincipal AppUserDetails userDetails
    ) {
        return profileService.getProfileByUserId(userDetails.getUserId());
    }

    @PutMapping("/me")
    public ProfileResponse updateMyProfile(
            @AuthenticationPrincipal AppUserDetails userDetails,
            @Valid @RequestBody UpdateProfileRequest request
    ) {
        return profileService.updateProfileByUserId(userDetails.getUserId(), request);
    }

    @PutMapping("/me/avatar")
    public ProfileResponse updateMyAvatar(
            @RequestPart("attachment") MultipartFile file,
            @AuthenticationPrincipal AppUserDetails userDetails
    ) {
        FileEntity res = fileService.uploadFileToDefaultBucket(file, userDetails.getUser());
        return profileService.updateProfileAvatarByUserId(
                userDetails.getUserId(),
                res
        );
    }

    @PutMapping("/me/banner")
    public ProfileResponse updateMyBanner(
            @RequestPart("attachment") MultipartFile file,
            @AuthenticationPrincipal AppUserDetails userDetails
    ) {
        FileEntity res = fileService.uploadFileToDefaultBucket(file, userDetails.getUser());
        return profileService.updateProfileBannerByUserId(
                userDetails.getUserId(),
                res
        );
    }
}
