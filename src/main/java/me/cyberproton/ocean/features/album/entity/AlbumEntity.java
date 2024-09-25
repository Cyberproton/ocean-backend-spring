package me.cyberproton.ocean.features.album.entity;

import jakarta.persistence.*;

import lombok.*;

import me.cyberproton.ocean.features.album.util.AlbumConstant;
import me.cyberproton.ocean.features.copyright.CopyrightEntity;
import me.cyberproton.ocean.features.file.FileEntity;
import me.cyberproton.ocean.features.recordlabel.RecordLabelEntity;
import me.cyberproton.ocean.features.track.entity.TrackEntity;
import me.cyberproton.ocean.features.user.UserEntity;

import org.hibernate.annotations.BatchSize;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity(name = "album")
@EntityListeners(AlbumListener.class)
public class AlbumEntity {
    @Id @GeneratedValue private Long id;

    @Enumerated(EnumType.STRING)
    private AlbumType type;

    @Column(length = AlbumConstant.MAX_ALBUM_NAME_LENGTH)
    private String name;

    @Column(length = AlbumConstant.MAX_ALBUM_DESCRIPTION_LENGTH)
    private String description;

    private Date releaseDate;

    @OneToMany private List<FileEntity> covers;

    @BatchSize(size = 20)
    @ManyToMany
    @JoinTable(
            name = "album_copyrights",
            joinColumns = @JoinColumn(name = "album_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "copy_id", referencedColumnName = "id"))
    private Set<CopyrightEntity> copyrights;

    @ManyToOne(fetch = FetchType.LAZY)
    private RecordLabelEntity recordLabel;

    @OneToMany(mappedBy = "album")
    private Set<TrackEntity> tracks;

    @ManyToMany
    @JoinTable(
            name = "user_saved_album",
            joinColumns = @JoinColumn(name = "album_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private Set<UserEntity> savedUsers;

    @OneToOne(mappedBy = "album", fetch = FetchType.LAZY)
    private AlbumAnalyticsEntity analytics;

    public void addSavedUser(UserEntity user) {
        if (this.savedUsers == null) {
            setSavedUsers(new HashSet<>());
        }
        this.savedUsers.add(user);
    }

    public void removeSavedUser(UserEntity user) {
        if (this.savedUsers == null) {
            return;
        }
        this.savedUsers.remove(user);
    }
}
