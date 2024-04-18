package me.cyberproton.ocean.seed;

import lombok.AllArgsConstructor;
import me.cyberproton.ocean.features.artist.Artist;
import me.cyberproton.ocean.features.artist.ArtistRepository;
import me.cyberproton.ocean.features.role.DefaultRole;
import me.cyberproton.ocean.features.user.User;
import me.cyberproton.ocean.features.user.UserRepository;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Component
public class ArtistSeeder {
    private final UserRepository userRepository;
    private final ArtistRepository artistRepository;
    private final Faker faker;

    public List<Artist> seed(List<User> users) {
        List<User> newUsers = userRepository
                .findAllWithRolesByIdInAndRolesName(users.stream().map(User::getId).toList(), DefaultRole.ARTIST.name());
        List<Artist> artists = new ArrayList<>();
        for (User user : newUsers) {
            artists.add(
                    Artist.builder()
                            .user(user)
                            .build()
            );
        }
        return artistRepository.saveAll(artists);
    }
}
