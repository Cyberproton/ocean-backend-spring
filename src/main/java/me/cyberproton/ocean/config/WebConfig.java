package me.cyberproton.ocean.config;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import me.cyberproton.ocean.annotations.V1ApiRestController;
import me.cyberproton.ocean.features.search.SearchQuery;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@AllArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final ExternalAppConfig externalAppConfig;

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        // This will add a prefix to all controllers annotated with @V1ApiController
        configurer.addPathPrefix(externalAppConfig.apiV1Path(), HandlerTypePredicate.forAnnotation(V1ApiRestController.class));
    }

    @Override
    public void addFormatters(@Nonnull FormatterRegistry registry) {
        registry.addConverter(new SearchQuery.StringToTypeConverter());
    }
}
