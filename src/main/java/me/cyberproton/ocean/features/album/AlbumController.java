package me.cyberproton.ocean.features.album;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import me.cyberproton.ocean.annotations.V1ApiRestController;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@AllArgsConstructor
@V1ApiRestController
@RequestMapping("/albums")
public class AlbumController {
    private final AlbumService albumService;

    @GetMapping
    public Set<AlbumResponse> getAlbums() {
        return albumService.getAlbums();
    }

    @GetMapping("/{id}")
    public AlbumResponse getAlbum(@PathVariable Long id) {
        return albumService.getAlbum(id);
    }

    @PostMapping
    public AlbumResponse createAlbum(@Valid @RequestBody CreateOrUpdateAlbumRequest request) {
        return albumService.createAlbum(request);
    }

    @PutMapping("/{id}")
    public AlbumResponse updateAlbum(@PathVariable Long id, @Valid @RequestBody CreateOrUpdateAlbumRequest request) {
        return albumService.updateAlbum(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteAlbum(@PathVariable Long id) {
        albumService.deleteAlbum(id);
    }
}
