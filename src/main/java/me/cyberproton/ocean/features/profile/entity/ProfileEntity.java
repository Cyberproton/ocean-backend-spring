package me.cyberproton.ocean.features.profile.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import me.cyberproton.ocean.features.artist.ArtistProfileListener;
import me.cyberproton.ocean.features.file.FileEntity;
import me.cyberproton.ocean.features.user.UserEntity;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "profile")
@EntityListeners({ProfileListener.class, ArtistProfileListener.class})
public class ProfileEntity {
    @Id private Long id;

    private String name;

    private String bio;

    @OneToOne private UserEntity user;

    @OneToOne private FileEntity avatar;

    @OneToOne private FileEntity banner;
}
