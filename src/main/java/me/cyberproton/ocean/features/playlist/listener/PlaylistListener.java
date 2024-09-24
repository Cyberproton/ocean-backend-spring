package me.cyberproton.ocean.features.playlist.listener;

import lombok.AllArgsConstructor;

import me.cyberproton.ocean.features.playlist.entity.PlaylistAnalyticsEntity;
import me.cyberproton.ocean.features.playlist.entity.PlaylistEntity;
import me.cyberproton.ocean.features.playlist.event.PlaylistPlayedEvent;
import me.cyberproton.ocean.features.playlist.event.PlaylistSavedEvent;
import me.cyberproton.ocean.features.playlist.repository.PlaylistAnalyticsRepository;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class PlaylistListener {
    private final PlaylistAnalyticsRepository playlistAnalyticsRepository;

    @EventListener
    public void onPlaylistPlayedEvent(PlaylistPlayedEvent event) {
        List<Long> playlistIds =
                event.getPlaylists().stream()
                        .map(PlaylistPlayedEvent.PlayedPlaylist::playlistId)
                        .collect(Collectors.toList());
        List<PlaylistAnalyticsEntity> analyticsEntityList =
                playlistAnalyticsRepository.findAllById(playlistIds);
        List<PlaylistAnalyticsEntity> newAnalyticsEntityList = new ArrayList<>();
        for (PlaylistPlayedEvent.PlayedPlaylist playlist : event.getPlaylists()) {
            long playedAt = playlist.playedAt();
            PlaylistAnalyticsEntity playlistAnalyticsEntity =
                    analyticsEntityList.stream()
                            .filter(
                                    playlistAnalyticsEntity1 ->
                                            playlistAnalyticsEntity1
                                                    .getId()
                                                    .equals(playlist.playlistId()))
                            .findFirst()
                            .orElseGet(
                                    () ->
                                            PlaylistAnalyticsEntity.builder()
                                                    .playlist(
                                                            PlaylistEntity.builder()
                                                                    .id(playlist.playlistId())
                                                                    .build())
                                                    .build());
            playlistAnalyticsEntity.setNumberOfPlays(
                    playlistAnalyticsEntity.getNumberOfPlays() + 1);
            playlistAnalyticsEntity.setPlayTotalTimestampInMinutes(
                    playlistAnalyticsEntity.getPlayTotalTimestampInMinutes() + (playedAt / 60000));
            newAnalyticsEntityList.add(playlistAnalyticsEntity);
        }
        playlistAnalyticsRepository.saveAll(newAnalyticsEntityList);
    }

    @EventListener
    public void onPlaylistSavedEvent(PlaylistSavedEvent event) {
        List<Long> playlistIds =
                event.getPlaylists().stream()
                        .map(PlaylistSavedEvent.SavedPlaylist::getPlaylistId)
                        .collect(Collectors.toList());
        List<PlaylistAnalyticsEntity> analyticsEntityList =
                playlistAnalyticsRepository.findAllById(playlistIds);
        List<PlaylistAnalyticsEntity> newAnalyticsEntityList = new ArrayList<>();
        for (PlaylistSavedEvent.SavedPlaylist playlist : event.getPlaylists()) {
            long savedAt = playlist.getSavedAt();
            PlaylistAnalyticsEntity playlistAnalyticsEntity =
                    analyticsEntityList.stream()
                            .filter(
                                    playlistAnalyticsEntity1 ->
                                            playlistAnalyticsEntity1
                                                    .getId()
                                                    .equals(playlist.getPlaylistId()))
                            .findFirst()
                            .orElseGet(
                                    () ->
                                            PlaylistAnalyticsEntity.builder()
                                                    .playlist(
                                                            PlaylistEntity.builder()
                                                                    .id(playlist.getPlaylistId())
                                                                    .build())
                                                    .build());
            playlistAnalyticsEntity.setNumberOfSaves(
                    playlistAnalyticsEntity.getNumberOfSaves()
                            + (event.getType() == PlaylistSavedEvent.Type.SAVED ? 1 : -1));
            playlistAnalyticsEntity.setSaveTotalTimestampInMinutes(
                    playlistAnalyticsEntity.getSaveTotalTimestampInMinutes()
                            + (savedAt / 60000)
                                    * (event.getType() == PlaylistSavedEvent.Type.SAVED ? 1 : -1));
            newAnalyticsEntityList.add(playlistAnalyticsEntity);
        }
        playlistAnalyticsRepository.saveAll(newAnalyticsEntityList);
    }
}
