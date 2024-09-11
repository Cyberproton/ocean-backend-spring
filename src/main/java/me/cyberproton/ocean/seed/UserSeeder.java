package me.cyberproton.ocean.seed;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import me.cyberproton.ocean.features.user.UserEntity;
import me.cyberproton.ocean.features.user.UserRepository;
import net.datafaker.Faker;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Profile("seeder")
@AllArgsConstructor
@Component
public class UserSeeder {
    private final UserRepository userRepository;

    public List<UserEntity> seed() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Faker faker = new Faker();
        List<UserEntity> users = new ArrayList<>();

        users.add(
                UserEntity.builder()
                        .email("admin@example.com")
                        .username(faker.internet().username())
                        .password(passwordEncoder.encode("123456"))
                        .build());

        for (int i = 0; i < 10; i++) {
            String username = faker.internet().username();
            users.add(
                    UserEntity.builder()
                            .email(username + "@example.com")
                            .username(username)
                            .password(passwordEncoder.encode("123456"))
                            .build());
        }

        userRepository.saveAll(users);
        return users;
    }
}
