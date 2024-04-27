package me.cyberproton.ocean.features.file;

import lombok.Builder;
import lombok.NonNull;

import java.io.OutputStream;

@Builder
public record StreamFileToOutputStreamRequest(
        @NonNull
        Long id,
        @NonNull
        OutputStream outputStream,
        String range,
        boolean closeStreamAfterFinish
) {
}
