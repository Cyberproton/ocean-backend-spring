package me.cyberproton.ocean.features.file;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record StreamFileToBodyRequest(@NonNull Long id, String range) {}
