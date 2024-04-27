package me.cyberproton.ocean.seed;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import me.cyberproton.ocean.features.playlist.*;
import me.cyberproton.ocean.features.track.Track;
import me.cyberproton.ocean.features.track.TrackRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Component
public class PlaylistTrackSeeder {
    private final PlaylistRepository playlistRepository;
    private final TrackRepository trackRepository;
    private final PlaylistTrackRepository playlistTrackRepository;

    @Transactional
    public void seed() {
        List<Playlist> playlists = playlistRepository.findAll();
        List<Track> tracks = trackRepository.findAll();
        List<PlaylistTrack> playlistTracks = new ArrayList<>();
        for (Playlist playlist : playlists) {
            List<Track> pts = SeedUtils.randomElements(tracks, 5);
            for (int i = 0; i < pts.size(); i++) {
                Track track = pts.get(i);
                playlistTracks.add(
                        PlaylistTrack.builder()
                                .id(
                                        PlaylistTrackKey.builder()
                                                .playlistId(playlist.getId())
                                                .trackId(track.getId())
                                                .build()
                                )
                                .playlist(playlist)
                                .track(track)
                                .trackPosition((long) i)
                                .build()
                );
            }
        }
        playlistTrackRepository.saveAll(playlistTracks);
    }
}
