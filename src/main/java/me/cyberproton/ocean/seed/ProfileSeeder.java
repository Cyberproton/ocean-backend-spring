package me.cyberproton.ocean.seed;

import lombok.AllArgsConstructor;

import me.cyberproton.ocean.features.file.FileEntity;
import me.cyberproton.ocean.features.file.FileService;
import me.cyberproton.ocean.features.profile.entity.ProfileEntity;
import me.cyberproton.ocean.features.profile.repository.ProfileRepository;
import me.cyberproton.ocean.features.user.UserEntity;

import net.datafaker.Faker;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Component
@Profile("seeder")
public class ProfileSeeder {
    private final ProfileRepository profileRepository;
    private final FileService fileService;
    private final Faker faker;
    private final ImageUploaderDownloader imageUploaderDownloader;

    public void seed(List<UserEntity> users) {
        List<ProfileEntity> profiles = new ArrayList<>();

        List<FileEntity> avatarFiles =
                imageUploaderDownloader.downloadAndUploadAll(
                        users.stream().map(user -> faker.avatar().image()).toList(),
                        users,
                        "profile-avatar",
                        true,
                        (fileEntity -> {
                            fileEntity.setWidth(300);
                            fileEntity.setHeight(300);
                        }));

        List<FileEntity> bannerFiles =
                imageUploaderDownloader.downloadAndUploadAll(
                        users.stream().map(user -> faker.avatar().image()).toList(),
                        users,
                        "profile-banner",
                        true,
                        (fileEntity -> {
                            fileEntity.setWidth(300);
                            fileEntity.setHeight(300);
                        }));

        for (int i = 0; i < users.size(); i++) {
            UserEntity user = users.get(i);
            FileEntity avatarFile = avatarFiles.get(i);
            FileEntity bannerFile = bannerFiles.get(i);
            profiles.add(
                    ProfileEntity.builder()
                            .id(user.getId())
                            .name(faker.name().fullName())
                            .bio(faker.lorem().paragraph(2))
                            .user(user)
                            .avatar(avatarFile)
                            .banner(bannerFile)
                            .build());
        }

        profileRepository.saveAll(profiles);
    }
}
