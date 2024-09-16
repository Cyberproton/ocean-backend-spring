package me.cyberproton.ocean.features.track.event;

import lombok.AllArgsConstructor;

import me.cyberproton.ocean.features.track.repository.TrackElasticRepository;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class TracksLikeChangeListener {
    private final TrackElasticRepository trackElasticRepository;

    @Async
    @EventListener
    public void onTracksLikeChange(TracksLikeChangeEvent event) {
        trackElasticRepository.changeNumberOfLikes(event.getTrackLikes());
    }
}
