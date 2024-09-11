package me.cyberproton.ocean.seed;

import lombok.AllArgsConstructor;

import me.cyberproton.ocean.features.album.entity.AlbumEntity;
import me.cyberproton.ocean.features.album.entity.AlbumType;
import me.cyberproton.ocean.features.album.repository.AlbumRepository;
import me.cyberproton.ocean.features.copyright.CopyrightEntity;
import me.cyberproton.ocean.features.file.FileEntity;
import me.cyberproton.ocean.features.recordlabel.RecordLabelEntity;
import me.cyberproton.ocean.features.user.UserEntity;

import net.datafaker.Faker;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Component
@Profile("seeder")
public class AlbumSeeder {
    private final AlbumRepository albumRepository;
    private final Faker faker;
    private final ImageUploaderDownloader imageUploaderDownloader;

    public List<AlbumEntity> seed(
            List<UserEntity> users,
            List<CopyrightEntity> copyrights,
            List<RecordLabelEntity> recordLabels) {
        int numberOfAlbums = 10;

        List<String> coverUrls = new ArrayList<>();
        for (int i = 0; i < numberOfAlbums; i++) {
            coverUrls.add(faker.avatar().image());
        }
        List<FileEntity> coverFiles =
                imageUploaderDownloader.downloadAndUploadAll(
                        coverUrls,
                        users,
                        "album-cover",
                        true,
                        fileEntity -> {
                            fileEntity.setWidth(300);
                            fileEntity.setHeight(300);
                        });

        List<AlbumEntity> albums = new ArrayList<>();
        for (int i = 0; i < numberOfAlbums; i++) {
            AlbumEntity album =
                    AlbumEntity.builder()
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
