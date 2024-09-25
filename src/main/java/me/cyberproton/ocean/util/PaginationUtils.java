package me.cyberproton.ocean.util;

import jakarta.annotation.Nullable;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class PaginationUtils {
    public static NextPreviousUrls generateNextPreviousUrls(
            String baseUrl, long limit, long offset, long total) {
        String next = null;
        String previous = null;
        
        if (offset + limit < total) {
            next = baseUrl + "?limit=" + limit + "&offset=" + (offset + limit);
        }
        if (offset - limit >= 0) {
            previous = baseUrl + "?limit=" + limit + "&offset=" + (offset - limit);
        }
        return new NextPreviousUrls(next, previous);
    }

    @RequiredArgsConstructor
    @Getter
    public static class NextPreviousUrls {
        @Nullable private final String next;
        @Nullable private final String previous;
    }
}
