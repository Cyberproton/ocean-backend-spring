package me.cyberproton.ocean.features.track;

import lombok.AllArgsConstructor;
import me.cyberproton.ocean.features.album.Album;
import me.cyberproton.ocean.features.album.AlbumRepository;
import me.cyberproton.ocean.features.artist.Artist;
import me.cyberproton.ocean.features.artist.ArtistRepository;
import me.cyberproton.ocean.util.ImageUrlMapper;
import org.springframework.context.ApplicationEventPublisher;
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
    private final ApplicationEventPublisher eventPublisher;
    private final ImageUrlMapper imageUrlMapper;

    public Set<TrackResponse> getTracks() {
        return trackRepository.findAllEagerly()
                              .stream()
                              .map(t -> TrackResponse.fromEntity(t, imageUrlMapper))
                              .collect(Collectors.toSet());
    }

    public TrackResponse getTrackById(Long id) {
        return trackRepository.findById(id)
                              .map(t -> TrackResponse.fromEntity(t, imageUrlMapper))
                              .orElse(null);
    }

    public TrackResponse createTrack(CreateOrUpdateTrackRequest request) {
        Album album = albumRepository.findById(request.albumId())
                                     .orElseThrow(() -> new ResponseStatusException(
                                             HttpStatus.NOT_FOUND,
                                             "Album not found"
                                     ));
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
        eventPublisher.publishEvent(new TrackEvent(TrackEvent.Type.CREATE, track));
        return TrackResponse.fromEntity(trackRepository.save(track), imageUrlMapper);
    }

    public TrackResponse updateTrack(Long id, CreateOrUpdateTrackRequest request) {
        Track track = trackRepository.findById(id)
                                     .orElseThrow(() -> new ResponseStatusException(
                                             HttpStatus.NOT_FOUND,
                                             "Track not found"
                                     ));
        Album album = albumRepository.findById(request.albumId())
                                     .orElseThrow(() -> new ResponseStatusException(
                                             HttpStatus.NOT_FOUND,
                                             "Album not found"
                                     ));
        Set<Artist> artists = Set.copyOf(artistRepository.findAllById(request.artistIds()));
        if (artists.size() != request.artistIds().size()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Some artists not found");
        }
        track.setName(request.name());
        track.setTrackNumber(request.trackNumber());
        track.setDuration(request.duration());
        track.setAlbum(album);
        track.setArtists(artists);
        Track saved = trackRepository.save(track);
        eventPublisher.publishEvent(new TrackEvent(TrackEvent.Type.UPDATE, saved));
        return TrackResponse.fromEntity(saved, imageUrlMapper);
    }

    public Long deleteTrack(Long id) {
        Track track = trackRepository.findById(id)
                                     .orElseThrow(() -> new ResponseStatusException(
                                             HttpStatus.NOT_FOUND,
                                             "Track not found"
                                     ));
        trackRepository.deleteById(id);
        eventPublisher.publishEvent(new TrackEvent(TrackEvent.Type.DELETE, track));
        return id;
    }
}
