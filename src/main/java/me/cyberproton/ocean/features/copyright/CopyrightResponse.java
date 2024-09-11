package me.cyberproton.ocean.features.copyright;

import lombok.Builder;

@Builder
public record CopyrightResponse(Long id, String text, CopyrightType type) {}
