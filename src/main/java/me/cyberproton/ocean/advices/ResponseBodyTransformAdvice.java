package me.cyberproton.ocean.advices;

import jakarta.annotation.Nonnull;
import java.util.Map;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class ResponseBodyTransformAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(
            @Nonnull MethodParameter returnType,
            @Nonnull Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(
            Object body,
            @Nonnull MethodParameter returnType,
            @Nonnull MediaType selectedContentType,
            @Nonnull Class<? extends HttpMessageConverter<?>> selectedConverterType,
            @Nonnull ServerHttpRequest request,
            @Nonnull ServerHttpResponse response) {
        if (body == null) {
            return null;
        }
        if (response instanceof ServletServerHttpResponse servletServerHttpResponse) {
            int status = servletServerHttpResponse.getServletResponse().getStatus();
            if (status >= 200 && status < 300) {
                return Map.of("data", body);
            }
        }
        return body;
    }
}
