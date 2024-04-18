package me.cyberproton.ocean.seed;

import lombok.AllArgsConstructor;
import me.cyberproton.ocean.features.genre.Genre;
import me.cyberproton.ocean.features.genre.GenreRepository;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Component
public class GenreSeeder {
    private final Faker faker;
    private final GenreRepository genreRepository;

    public List<Genre> seed() {
        int numberOfGenres = 10;

        List<Genre> genres = new ArrayList<>();
        List<String> genreNames = List.of(
                "Pop",
                "Rock",
                "Jazz",
                "Blues",
                "Hip Hop",
                "Rap",
                "Country",
                "Classical",
                "Electronic",
                "Reggae"
        );
        for (String genreName : genreNames) {
            genres.add(
                    Genre.builder()
                            .name(genreName)
                            .build()
            );
        }

        return genreRepository.saveAll(genres);
    }
}
