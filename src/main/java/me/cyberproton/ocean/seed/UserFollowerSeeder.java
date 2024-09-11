package me.cyberproton.ocean.seed;

import java.util.HashSet;
import java.util.List;
import lombok.AllArgsConstructor;
import me.cyberproton.ocean.features.user.UserEntity;
import me.cyberproton.ocean.features.user.UserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Profile("seeder")
@Component
public class UserFollowerSeeder {
    private final UserRepository userRepository;

    public void seed(List<UserEntity> users) {
        for (int i = 0; i < users.size(); i++) {
            List<UserEntity> followers = SeedUtils.randomElements(users, SeedUtils.randomInt(1, 5));
            UserEntity user = users.get(i);
            user.setFollowers(new HashSet<>(followers));
        }
        userRepository.saveAll(users);
    }
}
