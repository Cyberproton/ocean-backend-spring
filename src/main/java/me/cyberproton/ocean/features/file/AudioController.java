package me.cyberproton.ocean.features.file;

import java.util.List;
import lombok.AllArgsConstructor;
import me.cyberproton.ocean.annotations.V1ApiRestController;
import org.springframework.http.HttpRange;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@AllArgsConstructor
@V1ApiRestController
@RequestMapping("/media")
public class AudioController {
    private final FileService fileService;

    @GetMapping(value = "{id}", produces = "audio/mp3")
    public ResponseEntity<StreamingResponseBody> getAudio(
            @PathVariable Long id, @RequestHeader(value = "Range", required = false) String range) {
        HttpRange httpRange;
        if (range != null) {
            try {
                List<HttpRange> ranges = HttpRange.parseRanges(range);
                if (ranges.size() > 1) {
                    throw new ResponseStatusException(
                            HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE,
                            "Multiple ranges are not supported");
                }
                httpRange = ranges.getFirst();
            } catch (IllegalArgumentException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid range format");
            }
        } else {
            httpRange = null;
        }
        StreamingResponseBody responseBody =
                fileService.streamToStreamingResponseBody(
                        StreamFileToBodyRequest.builder().id(id).range(range).build());
        FileEntity file = fileService.getFile(id);
        // Parse size from range
        long size =
                range == null
                        ? file.getSize()
                        : httpRange.getRangeEnd(file.getSize())
                                - httpRange.getRangeStart(file.getSize())
                                + 1;
        String contentRange =
                range == null
                        ? null
                        : "bytes="
                                + httpRange.getRangeStart(file.getSize())
                                + "-"
                                + httpRange.getRangeEnd(file.getSize())
                                + "/"
                                + file.getSize();
        String acceptRanges = range == null ? null : "bytes";
        HttpStatus status = range == null ? HttpStatus.OK : HttpStatus.PARTIAL_CONTENT;
        return ResponseEntity.status(status)
                .headers(
                        b -> {
                            b.setContentLength(size);
                            b.setContentType(MediaType.parseMediaType(file.getMimetype()));
                            b.set("Accept-Ranges", acceptRanges);
                            b.set("Content-Range", contentRange);
                        })
                .body(responseBody);
    }
}
