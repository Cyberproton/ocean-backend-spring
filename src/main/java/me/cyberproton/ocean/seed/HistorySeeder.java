package me.cyberproton.ocean.seed;

import lombok.AllArgsConstructor;

import me.cyberproton.ocean.features.album.entity.AlbumEntity;
import me.cyberproton.ocean.features.artist.ArtistEntity;
import me.cyberproton.ocean.features.history.entity.HistoryEntity;
import me.cyberproton.ocean.features.history.entity.HistoryType;
import me.cyberproton.ocean.features.history.repository.HistoryRepository;
import me.cyberproton.ocean.features.playlist.entity.PlaylistEntity;
import me.cyberproton.ocean.features.track.entity.TrackEntity;
import me.cyberproton.ocean.features.user.UserEntity;

import net.datafaker.Faker;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Component
@Profile("seeder")
public class HistorySeeder {
    private final HistoryRepository historyRepository;
    private final Faker faker;

    public List<HistoryEntity> seed(
            List<UserEntity> users,
            List<ArtistEntity> artists,
            List<AlbumEntity> albums,
            List<PlaylistEntity> playlists,
            List<TrackEntity> tracks) {
        List<HistoryEntity> histories = new ArrayList<>();
        for (UserEntity user : users) {
            int numberOfArtistHistories = faker.random().nextInt(0, artists.size());
            List<ArtistEntity> selectedArtists =
                    SeedUtils.randomElements(artists, numberOfArtistHistories);
            List<HistoryEntity> histories1 =
                    historyRepository.saveAll(
                            selectedArtists.stream()
                                    .map(
                                            artist ->
                                                    HistoryEntity.builder()
                                                            .type(HistoryType.ARTIST)
                                                            .user(user)
                                                            .artist(artist)
                                                            .updatedAt(faker.date().birthday())
                                                            .build())
                                    .toList());
            histories.addAll(histories1);

            int numberOfAlbumHistories = faker.random().nextInt(0, albums.size());
            List<AlbumEntity> selectedAlbums =
                    SeedUtils.randomElements(albums, numberOfAlbumHistories);
            List<HistoryEntity> histories2 =
                    historyRepository.saveAll(
                            selectedAlbums.stream()
                                    .map(
                                            album ->
                                                    HistoryEntity.builder()
                                                            .type(HistoryType.ALBUM)
                                                            .user(user)
                                                            .album(album)
                                                            .updatedAt(faker.date().birthday())
                                                            .build())
                                    .toList());
            histories.addAll(histories2);

            int numberOfPlaylistHistories = faker.random().nextInt(0, playlists.size());
            List<PlaylistEntity> selectedPlaylists =
                    SeedUtils.randomElements(playlists, numberOfPlaylistHistories);
            List<HistoryEntity> histories3 =
                    historyRepository.saveAll(
                            selectedPlaylists.stream()
                                    .map(
                                            playlist ->
                                                    HistoryEntity.builder()
                                                            .type(HistoryType.PLAYLIST)
                                                            .user(user)
                                                            .playlist(playlist)
                                                            .updatedAt(faker.date().birthday())
                                                            .build())
                                    .toList());
            histories.addAll(histories3);

            int numberOfTrackHistories = faker.random().nextInt(0, tracks.size());
            List<TrackEntity> selectedTracks =
                    SeedUtils.randomElements(tracks, numberOfTrackHistories);
            List<HistoryEntity> histories4 =
                    historyRepository.saveAll(
                            selectedTracks.stream()
                                    .map(
                                            track ->
                                                    HistoryEntity.builder()
                                                            .type(HistoryType.TRACK)
                                                            .user(user)
                                                            .track(track)
                                                            .updatedAt(faker.date().birthday())
                                                            .build())
                                    .toList());
            histories.addAll(histories4);
        }

        return histories;
    }
}
