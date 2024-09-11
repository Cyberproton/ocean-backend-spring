package me.cyberproton.ocean.features.artist;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;

import lombok.AllArgsConstructor;

import me.cyberproton.ocean.features.profile.entity.ProfileEntity;
import me.cyberproton.ocean.features.user.UserElasticRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class ArtistProfileListener {
    private static final Logger log = LoggerFactory.getLogger(ArtistProfileListener.class);
    private final UserElasticRepository userElasticRepository;

    @PostPersist
    @PostUpdate
    public void onArtistProfileUpdated(ProfileEntity profile) {
        try {
            ArtistEntity artist = profile.getUser().getArtist();
            if (artist == null) {
                return;
            }
            userElasticRepository.save(profile.getUser());
        } catch (Exception e) {
            log.error("Failed to save artist document to Elasticsearch", e);
        }
    }
}
