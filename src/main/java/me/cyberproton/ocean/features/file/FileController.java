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
        FileEntity file = fileService.getFile(id);
        return FileResponse.builder()
                .id(file.getId())
                .name(file.getName())
                .mimetype(file.getMimetype())
                .path(file.getPath())
                .size(file.getSize())
                .owner(file.getOwner().getId())
                .createdAt(file.getCreatedAt())
                .build();
    }

    @PostMapping("/upload")
    public FileResponse upload(
            @RequestPart("attachment") MultipartFile file,
            @AuthenticationPrincipal AppUserDetails userDetails) {
        FileEntity res = fileService.uploadFileToDefaultBucket(file, userDetails.getUser());
        return FileResponse.builder()
                .id(res.getId())
                .name(res.getName())
                .mimetype(res.getMimetype())
                .path(res.getPath())
                .size(res.getSize())
                .owner(res.getOwner().getId())
                .createdAt(res.getCreatedAt())
                .build();
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<StreamingResponseBody> download(@PathVariable Long id) {
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
                                    ContentDisposition.attachment()
                                            .filename(file.getName())
                                            .build());
                        })
                .body(responseBody);
    }
}
