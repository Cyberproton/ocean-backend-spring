package me.cyberproton.ocean.features.track;

import lombok.AllArgsConstructor;
import me.cyberproton.ocean.features.album.Album;
import me.cyberproton.ocean.features.album.AlbumRepository;
import me.cyberproton.ocean.features.artist.Artist;
import me.cyberproton.ocean.features.artist.ArtistRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class TrackService {
    private final TrackRepository trackRepository;
    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;

    public Set<TrackResponse> getTracks() {
        return trackRepository.findAll()
                .stream()
                .map(TrackResponse::fromEntity)
                .collect(Collectors.toSet());
    }

    public TrackResponse getTrackById(Long id) {
        return trackRepository.findById(id)
                .map(TrackResponse::fromEntity)
                .orElse(null);
    }

    public TrackResponse createTrack(CreateOrUpdateTrackRequest request) {
        Album album = albumRepository.findById(request.albumId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Album not found"));
        Set<Artist> artists = Set.copyOf(artistRepository.findAllById(request.artistIds()));
        if (artists.size() != request.artistIds().size()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Some artists not found");
        }
        Track track = Track.builder()
                .name(request.name())
                .trackNumber(request.trackNumber())
                .duration(request.duration())
                .album(album)
                .artists(artists)
                .build();
        return TrackResponse.fromEntity(trackRepository.save(track));
    }

    public TrackResponse updateTrack(Long id, CreateOrUpdateTrackRequest request) {
        Track track = trackRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Track not found"));
        Album album = albumRepository.findById(request.albumId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Album not found"));
        Set<Artist> artists = Set.copyOf(artistRepository.findAllById(request.artistIds()));
        if (artists.size() != request.artistIds().size()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Some artists not found");
        }
        track.setName(request.name());
        track.setTrackNumber(request.trackNumber());
        track.setDuration(request.duration());
        track.setAlbum(album);
        track.setArtists(artists);
        return TrackResponse.fromEntity(trackRepository.save(track));
    }

    public Long deleteTrack(Long id) {
        trackRepository.deleteById(id);
        return id;
    }
}