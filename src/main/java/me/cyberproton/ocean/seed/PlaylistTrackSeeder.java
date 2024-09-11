package me.cyberproton.ocean.seed;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import me.cyberproton.ocean.features.playlist.entity.PlaylistEntity;
import me.cyberproton.ocean.features.playlist.entity.PlaylistTrackEntity;
import me.cyberproton.ocean.features.playlist.entity.PlaylistTrackKey;
import me.cyberproton.ocean.features.playlist.repository.PlaylistRepository;
import me.cyberproton.ocean.features.playlist.repository.PlaylistTrackRepository;
import me.cyberproton.ocean.features.track.entity.TrackEntity;
import me.cyberproton.ocean.features.track.repository.TrackRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
@Profile("seeder")
public class PlaylistTrackSeeder {
    private final PlaylistRepository playlistRepository;
    private final TrackRepository trackRepository;
    private final PlaylistTrackRepository playlistTrackRepository;

    @Transactional
    public void seed() {
        List<PlaylistEntity> playlists = playlistRepository.findAll();
        List<TrackEntity> tracks = trackRepository.findAll();
        List<PlaylistTrackEntity> playlistTracks = new ArrayList<>();
        for (PlaylistEntity playlist : playlists) {
            List<TrackEntity> pts = SeedUtils.randomElements(tracks, 5);
            for (int i = 0; i < pts.size(); i++) {
                TrackEntity track = pts.get(i);
                playlistTracks.add(
                        PlaylistTrackEntity.builder()
                                .id(
                                        PlaylistTrackKey.builder()
                                                .playlistId(playlist.getId())
                                                .trackId(track.getId())
                                                .build())
                                .playlist(playlist)
                                .track(track)
                                .trackPosition((long) i)
                                .build());
            }
        }
        playlistTrackRepository.saveAll(playlistTracks);
    }
}
