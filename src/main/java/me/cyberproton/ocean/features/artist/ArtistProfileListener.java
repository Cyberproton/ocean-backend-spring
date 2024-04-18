package me.cyberproton.ocean.features.artist;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
import lombok.AllArgsConstructor;
import me.cyberproton.ocean.features.profile.Profile;
import me.cyberproton.ocean.features.profile.ProfileDocument;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Component;

import java.util.Optional;

@AllArgsConstructor
@Component
public class ArtistProfileListener {
    private final ElasticsearchOperations elasticsearchOperations;
    @Lazy
    private final ArtistRepository artistRepository;

    @PostPersist
    @PostUpdate
    public void onArtistProfileUpdated(Profile profile) {
        Optional<Artist> artistOpt = artistRepository.findByUserProfileId(profile.getId());
        if (artistOpt.isEmpty()) {
            return;
        }
        Artist artist = artistOpt.get();
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
    }
}
