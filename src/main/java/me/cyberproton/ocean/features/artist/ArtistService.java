package me.cyberproton.ocean.features.artist;

import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@AllArgsConstructor
@Service
public class ArtistService {
    private final ArtistRepository artistRepository;

    public Set<ArtistResponse> getArtists() {
        return artistRepository.findAll().stream()
                .map(ArtistResponse::fromEntity)
                .collect(Collectors.toSet());
    }

    public ArtistResponse getArtistById(Long id) {
        return artistRepository
                .findById(id)
                .map(ArtistResponse::fromEntity)
                .orElseThrow(
                        () ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND, "Artist not found"));
    }
}
