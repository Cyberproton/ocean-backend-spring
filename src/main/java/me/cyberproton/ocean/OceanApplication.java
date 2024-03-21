package me.cyberproton.ocean;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class OceanApplication {
    public static void main(String[] args) {
        SpringApplication.run(OceanApplication.class, args);
    }
}
