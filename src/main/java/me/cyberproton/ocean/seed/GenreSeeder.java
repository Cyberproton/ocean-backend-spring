package me.cyberproton.ocean.seed;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import me.cyberproton.ocean.features.genre.GenreEntity;
import me.cyberproton.ocean.features.genre.GenreRepository;
import net.datafaker.Faker;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
@Profile("seeder")
public class GenreSeeder {
    private final Faker faker;
    private final GenreRepository genreRepository;

    public List<GenreEntity> seed() {
        int numberOfGenres = 10;

        List<GenreEntity> genres = new ArrayList<>();
        List<String> genreNames =
                List.of(
                        "Pop",
                        "Rock",
                        "Jazz",
                        "Blues",
                        "Hip Hop",
                        "Rap",
                        "Country",
                        "Classical",
                        "Electronic",
                        "Reggae");
        for (String genreName : genreNames) {
            genres.add(GenreEntity.builder().name(genreName).build());
        }

        return genreRepository.saveAll(genres);
    }
}
