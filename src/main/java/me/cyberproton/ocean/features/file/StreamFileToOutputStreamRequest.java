package me.cyberproton.ocean.features.file;

import java.io.OutputStream;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record StreamFileToOutputStreamRequest(
        @NonNull Long id,
        @NonNull OutputStream outputStream,
        String range,
        boolean closeStreamAfterFinish) {}
