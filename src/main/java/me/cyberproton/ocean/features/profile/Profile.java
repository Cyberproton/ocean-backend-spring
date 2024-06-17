package me.cyberproton.ocean.features.profile;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import me.cyberproton.ocean.features.artist.ArtistProfileListener;
import me.cyberproton.ocean.features.file.FileEntity;
import me.cyberproton.ocean.features.user.User;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(ArtistProfileListener.class)
public class Profile {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String bio;

    @OneToOne
    private User user;

    @OneToOne
    private FileEntity avatar;

    @OneToOne
    private FileEntity banner;
}
