package me.cyberproton.ocean;

import me.cyberproton.ocean.features.user.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@ConfigurationPropertiesScan
public class OceanApplication {

    public static void main(String[] args) {
        final ConfigurableApplicationContext context = SpringApplication.run(OceanApplication.class, args);
        final UserRepository userRepository = context.getBean(UserRepository.class);
    }

}
