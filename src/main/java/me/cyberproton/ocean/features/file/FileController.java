package me.cyberproton.ocean.features.file;

import lombok.AllArgsConstructor;
import me.cyberproton.ocean.annotations.V1ApiRestController;
import me.cyberproton.ocean.config.AppUserDetails;
import org.springframework.http.ContentDisposition;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@AllArgsConstructor
@V1ApiRestController
@RequestMapping("/files")
public class FileController {
    private final FileService fileService;

    @GetMapping("/{id}")
    public FileResponse getFile(@PathVariable Long id) {
        return fileService.getFile(id);
    }

    @PostMapping("/upload")
    public FileResponse upload(
            @RequestPart("attachment") MultipartFile file,
            @AuthenticationPrincipal AppUserDetails userDetails
    ) {
        return fileService.uploadFileToDefaultBucket(file, userDetails.getUser());
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<StreamingResponseBody> download(@PathVariable Long id) {
        StreamingResponseBody responseBody =
                outputStream -> fileService.downloadAndStreamToOutput(
                        id,
                        outputStream,
                        true
                );
        FileResponse file = fileService.getFile(id);
        return ResponseEntity.ok().headers(b -> {
            b.setContentLength(file.getSize());
            b.setContentType(MediaType.parseMediaType(file.getMimetype()));
            b.setContentDisposition(
                    ContentDisposition
                            .attachment()
                            .filename(file.getName())
                            .build()
            );
        }).body(responseBody);
    }
}
