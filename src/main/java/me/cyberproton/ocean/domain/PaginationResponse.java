package me.cyberproton.ocean.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import me.cyberproton.ocean.util.PaginationUtils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchHits;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
@Builder
@Data
public final class PaginationResponse<T> {
    @Builder.Default private final long limit = 20;
    @Builder.Default private final long offset = 0;
    private final String next;
    private final String previous;
    private final long total;
    private final Collection<T> items;

    public static <T> PaginationResponse<T> empty() {
        return PaginationResponse.<T>builder().items(List.of()).limit(0).offset(0).total(0).build();
    }

    public static <T> PaginationResponse<T> of(
            Collection<T> items, long limit, long offset, long total, String baseUrl) {
        PaginationUtils.NextPreviousUrls urls =
                PaginationUtils.generateNextPreviousUrls(baseUrl, limit, offset, total);

        return PaginationResponse.<T>builder()
                .items(items)
                .limit(limit)
                .offset(offset)
                .total(total)
                .next(urls.getNext())
                .previous(urls.getPrevious())
                .build();
    }

    public static <T> PaginationResponse<T> fromPage(Page<T> page, String next, String previous) {
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

    public static <T> PaginationResponse<T> fromPage(Page<T> page, String baseUrl) {
        Pageable pageable = page.getPageable();
        String next =
                page.getTotalElements() > pageable.getOffset() + pageable.getPageSize()
                        ? baseUrl
                                + "?limit="
                                + pageable.getPageSize()
                                + "&offset="
                                + (pageable.getOffset() + pageable.getPageSize())
                        : null;
        String previous =
                pageable.getOffset() > 0
                        ? baseUrl
                                + "?limit="
                                + pageable.getPageSize()
                                + "&offset="
                                + Math.max(0, pageable.getOffset() - pageable.getPageSize())
                        : null;
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
            String previous) {
        return PaginationResponse.<T>builder()
                .items(hits.stream().map(h -> mapper.apply(h.getContent())).toList())
                .limit(limit)
                .offset(offset)
                .total(hits.getTotalHits())
                .next(next)
                .previous(previous)
                .build();
    }

    public <O> PaginationResponse<O> map(Function<? super T, ? extends O> mapper) {
        return PaginationResponse.<O>builder()
                .items(items.stream().map(mapper).collect(Collectors.toList()))
                .limit(limit)
                .offset(offset)
                .total(total)
                .next(next)
                .previous(previous)
                .build();
    }
}
