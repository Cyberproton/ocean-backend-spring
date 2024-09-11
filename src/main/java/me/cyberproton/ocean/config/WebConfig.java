package me.cyberproton.ocean.config;

import jakarta.annotation.Nonnull;

import lombok.AllArgsConstructor;

import me.cyberproton.ocean.annotations.V1ApiRestController;
import me.cyberproton.ocean.features.auth.configs.ExternalAuthConfig;
import me.cyberproton.ocean.features.search.SearchQuery;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@AllArgsConstructor
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    private final ExternalAppConfig externalAppConfig;
    private final ExternalAuthConfig externalAuthConfig;

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        // This will add a prefix to all controllers annotated with @V1ApiController
        configurer.addPathPrefix(
                externalAppConfig.apiV1Path(),
                HandlerTypePredicate.forAnnotation(V1ApiRestController.class));
    }

    @Override
    public void addFormatters(@Nonnull FormatterRegistry registry) {
        registry.addConverter(new SearchQuery.StringToTypeConverter());
    }

    @Override
    public void addCorsMappings(@Nonnull CorsRegistry registry) {
        if (externalAuthConfig.disableCors()) {
            registry.addMapping("/**")
                    .allowedMethods(CorsConfiguration.ALL)
                    .allowedHeaders(CorsConfiguration.ALL)
                    .allowedOriginPatterns(CorsConfiguration.ALL);
        }
    }
}
