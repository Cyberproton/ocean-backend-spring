package me.cyberproton.ocean.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchHits;

import java.util.Collection;
import java.util.function.Function;

@AllArgsConstructor
@Builder
@Data
public final class PaginationResponse<T> {
    @Builder.Default
    private final long limit = 20;
    @Builder.Default
    private final long offset = 0;
    private final String next;
    private final String previous;
    private final long total;
    private final Collection<T> items;

    public static <T> PaginationResponse<T> fromPage(
            Page<T> page,
            String next,
            String previous
    ) {
        Pageable pageable = page.getPageable();
        return PaginationResponse.<T>builder()
                .items(page.getContent())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .total(page.getTotalElements())
                .next(next)
                .previous(previous)
                .build();
    }

    public static <T> PaginationResponse<T> fromSearchHits(
            SearchHits<?> hits,
            Function<Object, T> mapper,
            long limit,
            long offset,
            String next,
            String previous
    ) {
        return PaginationResponse.<T>builder()
                .items(hits.stream().map(h -> mapper.apply(h.getContent())).toList())
                .limit(limit)
                .offset(offset)
                .total(hits.getTotalHits())
                .next(next)
                .previous(previous)
                .build();
    }
}
