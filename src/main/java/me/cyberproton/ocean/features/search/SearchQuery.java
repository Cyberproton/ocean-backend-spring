package me.cyberproton.ocean.features.search;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.convert.converter.Converter;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SearchQuery {
    @Size(min = 1, max = 100)
    private String query;

    // Comma separated list of types
    @Size(min = 1, max = Type.SIZE)
    private Set<Type> type;

    @Min(1)
    @Max(100)
    @Builder.Default
    private Integer limit = 20;

    @Min(0)
    @Max(1000)
    @Builder.Default
    private Integer offset = 0;

    public enum Type {
        ALBUM,
        ARTIST,
        PLAYLIST,
        TRACK;

        public static final int SIZE = 4;
    }

    public static class StringToTypeConverter implements Converter<String, Type> {
        @Override
        public Type convert(String source) {
            return Type.valueOf(source.toUpperCase());
        }
    }
}
