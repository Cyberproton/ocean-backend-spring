package me.cyberproton.ocean.features.artist;

import java.util.Set;
import lombok.AllArgsConstructor;
import me.cyberproton.ocean.annotations.V1ApiRestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@AllArgsConstructor
@V1ApiRestController
@RequestMapping("/artists")
public class ArtistController {
    private final ArtistService artistService;

    @GetMapping
    public Set<ArtistResponse> getArtists() {
        return artistService.getArtists();
    }

    @GetMapping("/{id}")
    public ArtistResponse getArtistById(@PathVariable Long id) {
        return artistService.getArtistById(id);
    }
}
