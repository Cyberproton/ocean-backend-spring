package me.cyberproton.ocean.seed;

import lombok.AllArgsConstructor;
import me.cyberproton.ocean.features.album.Album;
import me.cyberproton.ocean.features.album.AlbumRepository;
import me.cyberproton.ocean.features.album.AlbumType;
import me.cyberproton.ocean.features.copyright.Copyright;
import me.cyberproton.ocean.features.file.FileEntity;
import me.cyberproton.ocean.features.recordlabel.RecordLabel;
import me.cyberproton.ocean.features.user.User;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Component
public class AlbumSeeder {
    private final AlbumRepository albumRepository;
    private final Faker faker;
    private final ImageUploaderDownloader imageUploaderDownloader;

    public List<Album> seed(List<User> users, List<Copyright> copyrights, List<RecordLabel> recordLabels) {
        int numberOfAlbums = 10;

        List<String> coverUrls = new ArrayList<>();
        for (int i = 0; i < numberOfAlbums; i++) {
            coverUrls.add(faker.avatar().image());
        }
        List<FileEntity> coverFiles = imageUploaderDownloader.downloadAndUploadAll(
                coverUrls,
                users,
                "album-cover",
                true,
                fileEntity -> {
                    fileEntity.setWidth(300);
                    fileEntity.setHeight(300);
                }
        );

        List<Album> albums = new ArrayList<>();
        for (int i = 0; i < numberOfAlbums; i++) {
            Album album = Album.builder()
                               .name(faker.brand().watch())
                               .description(faker.lorem().paragraph())
                               .type(SeedUtils.randomElement(AlbumType.values()))
                               .copyrights(Set.copyOf(SeedUtils.randomElements(copyrights, 2)))
                               .covers(List.of(coverFiles.get(i)))
                               .recordLabel(SeedUtils.randomElement(recordLabels))
                               .releaseDate(faker.date().birthday())
                               .build();
            albums.add(album);
        }
        return albumRepository.saveAll(albums);
    }
}
