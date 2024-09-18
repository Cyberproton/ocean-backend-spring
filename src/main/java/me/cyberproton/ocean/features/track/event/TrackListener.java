package me.cyberproton.ocean.features.track.event;

import lombok.AllArgsConstructor;

import me.cyberproton.ocean.features.track.repository.CustomTrackElasticRepository;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class TrackListener {
    private final CustomTrackElasticRepository customTrackElasticRepository;

    @Async
    @EventListener
    public void onTracksLikeChange(TracksLikeChangeEvent event) {
        customTrackElasticRepository.changeNumberOfLikes(event.getTrackLikeDtos());
    }

    @Async
    @EventListener
    public void onTrackPlayChange(TrackPlayChangeEvent event) {
        customTrackElasticRepository.changeNumberOfPlays(event.getTrackPlayDtos());
    }
}
