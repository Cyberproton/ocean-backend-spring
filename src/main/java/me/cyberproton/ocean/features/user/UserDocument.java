package me.cyberproton.ocean.features.user;

import jakarta.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import me.cyberproton.ocean.features.artist.ArtistDocument;
import me.cyberproton.ocean.features.profile.entity.ProfileDocument;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Document(indexName = "user")
public class UserDocument {
    @Id private Long id;

    private String username;

    private String email;

    private String password;

    private boolean isLocked;

    private boolean isEmailVerified;

    // From UserEntity.following
    private long numberOfFollowing;

    // From UserEntity.followers
    private long numberOfFollowers;

    @Field(type = FieldType.Object)
    private ProfileDocument profile;

    @Field(type = FieldType.Object)
    private ArtistDocument artist;
}
