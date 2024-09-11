package me.cyberproton.ocean.seed;

import net.datafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("seeder")
@Configuration
public class SeedConfig {
    @Bean
    public Faker faker() {
        return new Faker();
    }
}
