package me.cyberproton.ocean.seed;

import lombok.AllArgsConstructor;
import me.cyberproton.ocean.features.user.User;
import me.cyberproton.ocean.features.user.UserRepository;
import net.datafaker.Faker;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Component
public class UserSeeder {
    private final UserRepository userRepository;

    public List<User> seed() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Faker faker = new Faker();
        List<User> users = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            String username = faker.internet().username();
            users.add(
                    User.builder()
                            .email(username + "@example.com")
                            .username(username)
                            .password(passwordEncoder.encode("123456"))
                            .build()
            );
        }

        userRepository.saveAll(users);
        return users;
    }
}
