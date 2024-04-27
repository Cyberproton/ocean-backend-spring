package me.cyberproton.ocean.seed;

import lombok.AllArgsConstructor;
import me.cyberproton.ocean.features.file.FileEntity;
import me.cyberproton.ocean.features.playlist.Playlist;
import me.cyberproton.ocean.features.playlist.PlaylistRepository;
import me.cyberproton.ocean.features.user.User;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Component
public class PlaylistSeeder {
    private final PlaylistRepository playlistRepository;
    private final Faker faker;
    private final ImageUploaderDownloader imageUploaderDownloader;

    public List<Playlist> seed(List<User> users) {
        int numberOfPlaylists = 10;

        List<String> imageUrls = new ArrayList<>();
        for (int i = 0; i < numberOfPlaylists; i++) {
            imageUrls.add(faker.internet().image());
        }
        List<FileEntity> fileEntities = imageUploaderDownloader.downloadAndUploadAll(
                imageUrls,
                users,
                "playlist-cover",
                true
        );

        List<Playlist> playlists = new ArrayList<>();
        for (int i = 0; i < numberOfPlaylists; i++) {
            playlists.add(
                    Playlist.builder()
                            .name(faker.music().genre())
                            .description(faker.lorem().paragraph())
                            .cover(fileEntities.get(i))
                            .isPublic(faker.random().nextBoolean())
                            .owner(SeedUtils.randomElement(users))
                            .build()
            );
        }

        return playlistRepository.saveAll(playlists);
    }
}
