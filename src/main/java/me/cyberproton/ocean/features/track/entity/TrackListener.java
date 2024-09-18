package me.cyberproton.ocean.features.track.entity;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;

import lombok.AllArgsConstructor;

import me.cyberproton.ocean.features.track.repository.CustomTrackElasticRepository;

@AllArgsConstructor
public class TrackListener {
    private final CustomTrackElasticRepository customTrackElasticRepository;

    @PostPersist
    @PostUpdate
    public void onTrackCreatedOrUpdated(TrackEntity track) {
        customTrackElasticRepository.save(track);
    }

    @PostRemove
    public void onTrackDeleted(TrackEntity track) {
        customTrackElasticRepository.delete(track);
    }
}
