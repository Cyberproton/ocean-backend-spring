package me.cyberproton.ocean.seed;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import me.cyberproton.ocean.features.album.Album;
import me.cyberproton.ocean.features.album.AlbumRepository;
import me.cyberproton.ocean.features.artist.Artist;
import me.cyberproton.ocean.features.artist.ArtistRepository;
import me.cyberproton.ocean.features.file.FileEntity;
import me.cyberproton.ocean.features.file.FileService;
import me.cyberproton.ocean.features.genre.Genre;
import me.cyberproton.ocean.features.genre.GenreRepository;
import me.cyberproton.ocean.features.track.Track;
import me.cyberproton.ocean.features.track.TrackRepository;
import me.cyberproton.ocean.features.user.User;
import me.cyberproton.ocean.features.user.UserRepository;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Component
public class TrackSeeder {
    private final UserRepository userRepository;
    private final ArtistRepository artistRepository;
    private final GenreRepository genreRepository;
    private final AlbumRepository albumRepository;
    private final TrackRepository trackRepository;
    private final Faker faker;
    private final FileService fileService;

    @Transactional
    public List<Track> seed() {
        List<User> users = userRepository.findAll();
        List<Artist> artists = artistRepository.findAll();
        List<Genre> genres = genreRepository.findAll();
        List<Album> albums = albumRepository.findAll();

        int numberOfTracks = 10;

        List<FileEntity> files = new ArrayList<>();
        for (int i = 0; i < numberOfTracks; i++) {
            files.add(
                    fileService.uploadFileToDefaultBucket(
                            new File("tmp" + File.separator + "track" + File.separator + "sample.mp3"),
                            users.getFirst()
                    )
            );
        }

        List<Track> tracks = new ArrayList<>();
        for (int i = 0; i < numberOfTracks; i++) {
            tracks.add(
                    Track.builder()
                         .name(faker.touhou().trackName())
                         .duration(6000)
                         .trackNumber(faker.random().nextInt(1, 3))
                         .album(SeedUtils.randomElement(albums))
                         .artists(Set.copyOf(SeedUtils.randomElements(artists, 2)))
                         .genres(Set.copyOf(SeedUtils.randomElements(genres, 2)))
                         .file(files.get(i))
                         .build()
            );
        }

        return trackRepository.saveAll(tracks);
    }
}
