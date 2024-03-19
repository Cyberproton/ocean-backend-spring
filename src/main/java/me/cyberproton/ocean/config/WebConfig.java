package me.cyberproton.ocean.config;

import me.cyberproton.ocean.annotations.V1ApiController;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        // This will add a prefix to all controllers annotated with @V1ApiController
        configurer.addPathPrefix("/api/v1", HandlerTypePredicate.forAnnotation(V1ApiController.class));
    }
}
