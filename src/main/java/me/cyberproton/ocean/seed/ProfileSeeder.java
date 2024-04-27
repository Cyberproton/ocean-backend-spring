package me.cyberproton.ocean.seed;

import lombok.AllArgsConstructor;
import me.cyberproton.ocean.features.file.FileEntity;
import me.cyberproton.ocean.features.file.FileService;
import me.cyberproton.ocean.features.profile.Profile;
import me.cyberproton.ocean.features.profile.ProfileRepository;
import me.cyberproton.ocean.features.user.User;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Component
public class ProfileSeeder {
    private final ProfileRepository profileRepository;
    private final FileService fileService;
    private final Faker faker;
    private final ImageUploaderDownloader imageUploaderDownloader;

    public void seed(List<User> users) {
        List<Profile> profiles = new ArrayList<>();

        List<FileEntity> avatarFiles = imageUploaderDownloader.downloadAndUploadAll(
                users.stream().map(user -> faker.avatar().image()).toList(),
                users,
                "profile-avatar",
                true
        );

        List<FileEntity> bannerFiles = imageUploaderDownloader.downloadAndUploadAll(
                users.stream().map(user -> faker.avatar().image()).toList(),
                users,
                "profile-banner",
                true
        );


        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            FileEntity avatarFile = avatarFiles.get(i);
            FileEntity bannerFile = bannerFiles.get(i);
            profiles.add(
                    Profile.builder()
                            .name(faker.name().fullName())
                            .bio(faker.lorem().paragraph(2))
                            .user(user)
                            .avatar(avatarFile)
                            .banner(bannerFile)
                            .build()
            );
        }

        profileRepository.saveAll(profiles);
    }
}
