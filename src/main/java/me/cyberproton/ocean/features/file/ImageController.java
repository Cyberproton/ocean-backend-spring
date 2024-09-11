package me.cyberproton.ocean.features.file;

import lombok.AllArgsConstructor;
import me.cyberproton.ocean.annotations.V1ApiRestController;
import org.springframework.http.ContentDisposition;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@AllArgsConstructor
@V1ApiRestController
@RequestMapping("/images")
public class ImageController {
    private final FileService fileService;

    @GetMapping("/{id}")
    public ResponseEntity<StreamingResponseBody> getById(@PathVariable Long id) {
        StreamingResponseBody responseBody =
                fileService.streamToStreamingResponseBody(
                        StreamFileToBodyRequest.builder().id(id).build());
        FileEntity file = fileService.getFile(id);
        return ResponseEntity.ok()
                .headers(
                        b -> {
                            b.setContentLength(file.getSize());
                            b.setContentType(MediaType.parseMediaType(file.getMimetype()));
                            b.setContentDisposition(
                                    ContentDisposition.inline().filename(file.getName()).build());
                        })
                .body(responseBody);
    }
}
