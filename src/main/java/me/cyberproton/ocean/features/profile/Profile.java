package me.cyberproton.ocean.features.profile;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.cyberproton.ocean.features.artist.ArtistProfileListener;
import me.cyberproton.ocean.features.file.FileEntity;
import me.cyberproton.ocean.features.user.User;

@Data
@Builder
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
