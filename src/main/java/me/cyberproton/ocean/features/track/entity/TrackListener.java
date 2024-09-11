package me.cyberproton.ocean.features.track.entity;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;

import lombok.AllArgsConstructor;

import me.cyberproton.ocean.features.track.repository.TrackElasticRepository;

@AllArgsConstructor
public class TrackListener {
    private final TrackElasticRepository trackElasticRepository;

    @PostPersist
    @PostUpdate
    public void onTrackCreatedOrUpdated(TrackEntity track) {
        trackElasticRepository.save(track);
    }

    @PostRemove
    public void onTrackDeleted(TrackEntity track) {
        trackElasticRepository.delete(track);
    }
}
