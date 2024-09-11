package me.cyberproton.ocean.domain;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import lombok.*;
import lombok.experimental.SuperBuilder;

import org.springframework.data.domain.Sort;

@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@ToString
public class BaseQuery {
    @Min(1)
    @Max(100)
    @Builder.Default
    private Integer limit = 20;

    @Min(0)
    @Max(Long.MAX_VALUE)
    @Builder.Default
    private Long offset = 0L;

    public OffsetBasedPageRequest toOffsetBasedPageable() {
        return OffsetBasedPageRequest.of(offset, limit);
    }

    public OffsetBasedPageRequest toOffsetBasedPageable(Sort sort) {
        return OffsetBasedPageRequest.of(offset, limit, sort);
    }
}
