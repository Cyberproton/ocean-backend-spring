package me.cyberproton.ocean.features.playlist;

import lombok.AllArgsConstructor;
import me.cyberproton.ocean.features.track.Track;
import me.cyberproton.ocean.features.track.TrackRepository;
import me.cyberproton.ocean.features.track.TrackResponse;
import me.cyberproton.ocean.features.user.User;
import me.cyberproton.ocean.features.user.UserRepository;
import me.cyberproton.ocean.util.ImageUrlMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class PlaylistService {
    private final PlaylistRepository playlistRepository;
    private final TrackRepository trackRepository;
    private final PlaylistTrackRepository playlistTrackRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final ImageUrlMapper imageUrlMapper;

    public Set<PlaylistResponse> getUserPlaylists(Long id) {
        return playlistRepository
                       .findByOwnerId(id)
                       .map(p -> p.stream().map(playlist -> PlaylistResponse.fromEntity(playlist, imageUrlMapper))
                                  .collect(Collectors.toSet()))
                       .orElseThrow();
    }

    public PlaylistResponse createPlaylist(CreateOrUpdatePlaylist createOrUpdatePlaylist, Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        Set<PlaylistTrack> tracks = null;
        if (createOrUpdatePlaylist.trackIds() != null) {
            List<Track> ts = trackRepository.findAllById(createOrUpdatePlaylist.trackIds());
            if (ts.size() != createOrUpdatePlaylist.trackIds().size()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Some tracks do not exist"
                );
            }
            tracks = ts
                             .stream()
                             .map(
                                     track -> PlaylistTrack.builder()
                                                           .track(track)
                                                           .trackPosition(0L)
                                                           .build()
                             )
                             .collect(Collectors.toSet());
        }
        Playlist playlist = Playlist.builder()
                                    .name(createOrUpdatePlaylist.name())
                                    .description(createOrUpdatePlaylist.description())
                                    .isPublic(createOrUpdatePlaylist.isPublic())
                                    .owner(user)
                                    .playlistTracks(tracks)
                                    .build();
        Playlist saved = playlistRepository.save(playlist);
        eventPublisher.publishEvent(new PlaylistEvent(PlaylistEvent.Type.CREATE, saved));
        return PlaylistResponse.fromEntity(saved, imageUrlMapper);
    }

    public PlaylistResponse updatePlaylist(Long playlistId, CreateOrUpdatePlaylist createOrUpdatePlaylist) {
        Set<PlaylistTrack> tracks = null;
        if (createOrUpdatePlaylist.trackIds() != null) {
            List<Track> ts = trackRepository.findAllById(createOrUpdatePlaylist.trackIds());
            if (ts.size() != createOrUpdatePlaylist.trackIds().size()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Some tracks do not exist"
                );
            }
            tracks = ts
                             .stream()
                             .map(
                                     track -> PlaylistTrack.builder()
                                                           .track(track)
                                                           .trackPosition(0L)
                                                           .build()
                             )
                             .collect(Collectors.toSet());
        }
        Playlist playlist = playlistRepository.findById(playlistId).orElseThrow();
        playlist.setName(createOrUpdatePlaylist.name());
        playlist.setDescription(createOrUpdatePlaylist.description());
        playlist.setPublic(createOrUpdatePlaylist.isPublic());
        playlist.setPlaylistTracks(tracks);
        Playlist saved = playlistRepository.save(playlist);
        eventPublisher.publishEvent(new PlaylistEvent(PlaylistEvent.Type.UPDATE, saved));
        return PlaylistResponse.fromEntity(saved, imageUrlMapper);
    }

    public void deletePlaylist(Long playlistId) {
        Playlist playlist = playlistRepository.findById(playlistId).orElseThrow();
        playlistRepository.deleteById(playlistId);
        eventPublisher.publishEvent(new PlaylistEvent(PlaylistEvent.Type.DELETE, playlist));
    }

    public Set<TrackResponse> addTracksToPlaylist(Long playlistId, AddPlaylistTrackRequest request) {
        Playlist playlist = playlistRepository.findById(playlistId).orElseThrow();
        List<Track> tracks = trackRepository.findAllById(request.trackIds());
        if (tracks.size() != request.trackIds().size()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Some tracks do not exist"
            );
        }
        List<PlaylistTrack> playlistTracks = playlistTrackRepository
                                                     .findAllByPlaylistIdOrderByTrackPosition(playlistId)
                                                     .orElseThrow();
        if (request.position() > playlistTracks.size()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Position is out of range"
            );
        }
        playlistTracks.addAll(request.position().intValue(), tracks
                                                                     .stream()
                                                                     .map(
                                                                             track -> PlaylistTrack.builder()
                                                                                                   .playlist(playlist)
                                                                                                   .track(track)
                                                                                                   .trackPosition(0L)
                                                                                                   .build()
                                                                     )
                                                                     .toList()
        );
        // Update track positions
        for (int i = request.position().intValue(); i < playlistTracks.size(); i++) {
            playlistTracks.get(i).setTrackPosition((long) i);
        }
        playlistTrackRepository.saveAll(playlistTracks);
        return trackRepository
                       .findAllByPlaylistTracksPlaylistIdOrderByPlaylistTracksTrackPosition(playlistId)
                       .map(t -> t.stream().map(tr -> TrackResponse.fromEntity(tr, imageUrlMapper))
                                  .collect(Collectors.toSet()))
                       .orElseThrow();
    }

    public Set<TrackResponse> removeTracksFromPlaylist(Long playlistId, List<Long> trackIds) {
        playlistRepository.findById(playlistId).orElseThrow();
        List<Track> tracks = trackRepository.findAllById(trackIds);
        if (tracks.size() != trackIds.size()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Some tracks do not exist"
            );
        }
        List<PlaylistTrack> playlistTracks = playlistTrackRepository
                                                     .findAllByPlaylistIdOrderByTrackPosition(playlistId)
                                                     .orElseThrow();
        playlistTracks.removeIf(playlistTrack -> trackIds.contains(playlistTrack.getTrack().getId()));
        // Update track positions
        for (int i = 0; i < playlistTracks.size(); i++) {
            playlistTracks.get(i).setTrackPosition((long) i);
        }
        playlistTrackRepository.saveAll(playlistTracks);
        return trackRepository
                       .findAllByPlaylistTracksPlaylistIdOrderByPlaylistTracksTrackPosition(playlistId)
                       .map(t -> t.stream().map(tr -> TrackResponse.fromEntity(tr, imageUrlMapper))
                                  .collect(Collectors.toSet()))
                       .orElseThrow();
    }

    public Set<TrackResponse> updatePlaylistTracksPosition(
            Long playlistId, UpdatePlaylistTrackPositionRequest updatePlaylistTrackPositionRequest
    ) {
        playlistRepository.findById(playlistId).orElseThrow();
        List<PlaylistTrack> playlistTracks = playlistTrackRepository
                                                     .findAllByPlaylistIdOrderByTrackPosition(playlistId)
                                                     .orElseThrow();
        long startPosition = updatePlaylistTrackPositionRequest.startPosition();
        long insertBeforePosition = updatePlaylistTrackPositionRequest.insertBeforePosition();
        long length = updatePlaylistTrackPositionRequest.length();

        if (startPosition >= playlistTracks.size() || insertBeforePosition >= playlistTracks.size()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Position is out of range"
            );
        }

        // Remove tracks from the playlist
        List<PlaylistTrack> reorderingTracks = new ArrayList<>();
        for (int i = (int) startPosition; i < startPosition + length; i++) {
            reorderingTracks.add(playlistTracks.get(i));
        }
        playlistTracks.removeAll(reorderingTracks);

        playlistTracks.addAll((int) insertBeforePosition, reorderingTracks);

        // Update track positions
        for (int i = 0; i < playlistTracks.size(); i++) {
            playlistTracks.get(i).setTrackPosition((long) i);
        }
        playlistTrackRepository.saveAll(playlistTracks);
        return trackRepository
                       .findAllByPlaylistTracksPlaylistIdOrderByPlaylistTracksTrackPosition(playlistId)
                       .map(t -> t.stream().map(tr -> TrackResponse.fromEntity(tr, imageUrlMapper))
                                  .collect(Collectors.toSet()))
                       .orElseThrow();
    }
}
