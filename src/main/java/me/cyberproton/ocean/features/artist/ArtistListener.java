package me.cyberproton.ocean.features.artist;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Lazy;

@AllArgsConstructor
public class ArtistListener {
    private final ArtistElasticRepository artistElasticRepository;
    @Lazy private final ArtistRepository artistRepository;

    @PostPersist
    @PostUpdate
    public void onCreatedUpdated(ArtistEntity artist) {
        artistElasticRepository.save(artist);
    }

    @PostRemove
    public void onDeleted(ArtistEntity entity) {
        artistElasticRepository.delete(entity);
    }
}
