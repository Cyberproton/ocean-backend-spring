package me.cyberproton.ocean.features.artist;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
import lombok.AllArgsConstructor;
import me.cyberproton.ocean.features.profile.Profile;
import me.cyberproton.ocean.features.profile.ProfileDocument;
import me.cyberproton.ocean.features.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class ArtistProfileListener {
    private static final Logger log = LoggerFactory.getLogger(ArtistProfileListener.class);
    private final ElasticsearchOperations elasticsearchOperations;
    @Lazy
    private final UserRepository userRepository;
    @Lazy
    private final ArtistRepository artistRepository;

    @PostPersist
    @PostUpdate
    public void onArtistProfileUpdated(Profile profile) {
        try {
            Artist artist = profile.getUser().getArtist();
            if (artist == null) {
                return;
            }
            ProfileDocument profileDocument = ProfileDocument.builder()
                                                             .id(profile.getId())
                                                             .name(profile.getName())
                                                             .bio(profile.getBio())
                                                             .avatarId(profile.getAvatar().getId())
                                                             .bannerId(profile.getBanner().getId())
                                                             .build();
            ArtistDocument artistDocument = ArtistDocument.builder()
                                                          .id(artist.getId())
                                                          .profile(profileDocument)
                                                          .build();


            elasticsearchOperations.save(artistDocument);
        } catch (Exception e) {
            log.error("Failed to save artist document to Elasticsearch", e);
        }
    }
}
