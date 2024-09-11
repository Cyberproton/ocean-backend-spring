package me.cyberproton.ocean.domain;

import java.io.Serial;
import java.io.Serializable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class OffsetBasedPageRequest implements Pageable, Serializable {
    @Serial private static final long serialVersionUID = -25822477129613575L;
    private final int limit;
    private final long offset;
    private final Sort sort;

    public OffsetBasedPageRequest(long offset, int limit, Sort sort) {
        if (offset < 0) {
            throw new IllegalArgumentException("Offset index must not be less than zero");
        }
        if (limit < 1) {
            throw new IllegalArgumentException("Limit must not be less than one");
        }
        this.limit = limit;
        this.offset = offset;
        this.sort = sort;
    }

    public static OffsetBasedPageRequest of(long offset, int limit) {
        return new OffsetBasedPageRequest(offset, limit, Sort.unsorted());
    }

    public static OffsetBasedPageRequest of(long offset, int limit, Sort sort) {
        return new OffsetBasedPageRequest(offset, limit, sort);
    }

    @Override
    public int getPageNumber() {
        return (int) offset / limit;
    }

    @Override
    public int getPageSize() {
        return limit;
    }

    @Override
    public long getOffset() {
        return offset;
    }

    @Override
    public Sort getSort() {
        return sort;
    }

    @Override
    public Pageable next() {
        return new OffsetBasedPageRequest(offset + getPageSize(), getPageSize(), getSort());
    }

    @Override
    public Pageable previousOrFirst() {
        return hasPrevious()
                ? new OffsetBasedPageRequest(offset - getPageSize(), getPageSize(), getSort())
                : first();
    }

    @Override
    public Pageable first() {
        return new OffsetBasedPageRequest(0, getPageSize(), getSort());
    }

    @Override
    public Pageable withPage(int pageNumber) {
        return new OffsetBasedPageRequest(
                (long) pageNumber * (long) getPageSize(), getPageSize(), getSort());
    }

    @Override
    public boolean hasPrevious() {
        return offset > limit;
    }
}
