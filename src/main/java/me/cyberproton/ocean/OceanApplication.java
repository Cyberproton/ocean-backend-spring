package me.cyberproton.ocean;

import com.blazebit.persistence.spring.data.repository.config.EnableBlazeRepositories;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication(exclude = {ThymeleafAutoConfiguration.class})
@EnableBlazeRepositories
@ConfigurationPropertiesScan
public class OceanApplication {
    public static void main(String[] args) {
        SpringApplication.run(OceanApplication.class, args);
    }
}
