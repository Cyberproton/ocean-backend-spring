package me.cyberproton.ocean.seed;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import me.cyberproton.ocean.features.artist.ArtistEntity;
import me.cyberproton.ocean.features.artist.ArtistRepository;
import me.cyberproton.ocean.features.role.DefaultRole;
import me.cyberproton.ocean.features.user.UserEntity;
import me.cyberproton.ocean.features.user.UserRepository;
import net.datafaker.Faker;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
@Profile("seeder")
public class ArtistSeeder {
    private final UserRepository userRepository;
    private final ArtistRepository artistRepository;
    private final Faker faker;

    public List<ArtistEntity> seed(List<UserEntity> users) {
        List<UserEntity> newUsers =
                userRepository.findAllWithRolesByIdInAndRolesName(
                        users.stream().map(UserEntity::getId).toList(), DefaultRole.ARTIST.name());
        List<ArtistEntity> artists = new ArrayList<>();
        for (UserEntity user : newUsers) {
            artists.add(ArtistEntity.builder().user(user).build());
        }
        return artistRepository.saveAll(artists);
    }
}
