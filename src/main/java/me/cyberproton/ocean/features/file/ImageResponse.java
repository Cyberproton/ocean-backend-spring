package me.cyberproton.ocean.features.file;

import lombok.Builder;

@Builder
public record ImageResponse(String url, double width, double height) {}
