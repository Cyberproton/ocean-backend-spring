package me.cyberproton.ocean.features.user;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import me.cyberproton.ocean.features.artist.ArtistDocument;
import me.cyberproton.ocean.features.artist.ArtistEntity;
import me.cyberproton.ocean.features.file.FileDocument;
import me.cyberproton.ocean.features.file.FileMapper;
import me.cyberproton.ocean.features.profile.entity.ProfileDocument;
import me.cyberproton.ocean.features.profile.entity.ProfileEntity;
import me.cyberproton.ocean.repository.AbstractElasticRepository;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Component;

@Component
public class UserElasticRepository extends AbstractElasticRepository<UserEntity, UserDocument> {
    private final FileMapper fileMapper;

    public UserElasticRepository(
            ElasticsearchOperations elasticsearchOperations, FileMapper fileMapper) {
        super(UserEntity.class, UserDocument.class, elasticsearchOperations);
        this.fileMapper = fileMapper;
    }

    @Nonnull
    @Override
    public UserDocument entityToDocument(UserEntity entity) {
        ProfileEntity profile = entity.getProfile();
        FileDocument avatar;
        FileDocument banner;
        ProfileDocument profileDocument = null;
        if (profile != null) {
            avatar = fileMapper.entityToDocument(profile.getAvatar());
            banner = fileMapper.entityToDocument(profile.getBanner());
            profileDocument =
                    ProfileDocument.builder()
                            .id(profile.getId())
                            .username(entity.getUsername())
                            .name(profile.getName())
                            .bio(profile.getBio())
                            .avatar(avatar)
                            .banner(banner)
                            .build();
        }

        ArtistEntity artist = entity.getArtist();
        ArtistDocument artistDocument =
                artist == null ? null : ArtistDocument.builder().id(artist.getId()).build();

        return UserDocument.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .isLocked(entity.isLocked())
                .isEmailVerified(entity.isEmailVerified())
                .numberOfFollowing(entity.getFollowing().size())
                .numberOfFollowers(entity.getFollowers().size())
                .profile(profileDocument)
                .artist(artistDocument)
                .build();
    }

    @Nullable
    @Override
    protected String getEntityId(UserEntity entity) {
        return "id";
    }
}
