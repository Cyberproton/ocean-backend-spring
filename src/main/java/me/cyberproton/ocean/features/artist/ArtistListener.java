package me.cyberproton.ocean.features.artist;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import lombok.AllArgsConstructor;
import me.cyberproton.ocean.features.profile.Profile;
import me.cyberproton.ocean.features.profile.ProfileDocument;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

@AllArgsConstructor
public class ArtistListener {
    private final ElasticsearchOperations elasticsearchOperations;
    @Lazy
    private final ArtistRepository artistRepository;

    @PostPersist
    @PostUpdate
    public void onCreatedUpdated(Artist artist) {
        Profile profile = artist.getUser().getProfile();
        ProfileDocument profileDocument = null;
        if (profile != null) {
            profileDocument = ProfileDocument.builder()
                    .id(profile.getId())
                    .name(profile.getName())
                    .bio(profile.getBio())
                    .avatarId(profile.getAvatar().getId())
                    .bannerId(profile.getBanner().getId())
                    .build();
        }
        ArtistDocument artistDocument = ArtistDocument.builder()
                .id(artist.getId())
                .profile(profileDocument)
                .build();

        elasticsearchOperations.save(artistDocument);
    }

    @PostRemove
    public void onDeleted(Artist entity) {
        elasticsearchOperations.delete(
                entity.getId().toString(),
                ArtistDocument.class
        );
    }
}
