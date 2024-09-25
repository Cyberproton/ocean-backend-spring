package me.cyberproton.ocean.features.track.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import lombok.*;

import me.cyberproton.ocean.features.album.entity.AlbumEntity;
import me.cyberproton.ocean.features.artist.ArtistEntity;
import me.cyberproton.ocean.features.file.FileEntity;
import me.cyberproton.ocean.features.genre.GenreEntity;
import me.cyberproton.ocean.features.playlist.entity.PlaylistTrackEntity;
import me.cyberproton.ocean.features.user.UserEntity;
import me.cyberproton.ocean.util.PersistenceUtils;

import org.hibernate.annotations.BatchSize;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity(name = "track")
@EntityListeners(TrackListener.class)
@NamedEntityGraph(
        name = "eager-track",
        attributeNodes = {
            @NamedAttributeNode(value = "artists", subgraph = "eager-artist"),
            @NamedAttributeNode(value = "album", subgraph = "eager-album")
        },
        subgraphs = {
            @NamedSubgraph(
                    name = "eager-artist",
                    attributeNodes = @NamedAttributeNode(value = "user", subgraph = "eager-user")),
            @NamedSubgraph(name = "eager-user", attributeNodes = @NamedAttributeNode("profile")),
            @NamedSubgraph(name = "eager-album", attributeNodes = @NamedAttributeNode("covers"))
        })
public class TrackEntity {
    @Id @GeneratedValue private Long id;

    private String name;

    private Integer trackNumber;

    private Integer duration;

    @OneToOne private FileEntity file;

    @ManyToMany
    @JoinTable(
            name = "track_genres",
            joinColumns = @JoinColumn(name = "track_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id", referencedColumnName = "id"))
    private Set<GenreEntity> genres;

    @Nullable @ManyToOne private AlbumEntity album;

    @BatchSize(size = 20)
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "tracks_artists",
            joinColumns = @JoinColumn(name = "track_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "artist_id", referencedColumnName = "id"))
    private Set<ArtistEntity> artists;

    @OneToMany(mappedBy = "track", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<PlaylistTrackEntity> playlistTracks;

    @ManyToMany
    @JoinTable(
            name = "track_likes",
            joinColumns = @JoinColumn(name = "track_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private Set<UserEntity> likedUsers;

    @OneToOne(mappedBy = "track", fetch = FetchType.LAZY)
    private TrackAnalyticsEntity analytics;

    public void addArtist(ArtistEntity artist) {
        artists.add(artist);
        artist.getTracks().add(this);
    }

    public void removeArtist(ArtistEntity artist) {
        artists.remove(artist);
        artist.getTracks().remove(this);
    }

    public void addLikedUser(UserEntity user) {
        likedUsers.add(user);
        if (PersistenceUtils.isLoaded(user.getLikedTracks())) {
            user.getLikedTracks().add(this);
        }
    }

    public void removeLikedUser(UserEntity user) {
        likedUsers.remove(user);
        if (PersistenceUtils.isLoaded(user.getLikedTracks())) {
            user.getLikedTracks().remove(this);
        }
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass =
                o instanceof HibernateProxy
                        ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass()
                        : o.getClass();
        Class<?> thisEffectiveClass =
                this instanceof HibernateProxy
                        ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass()
                        : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        TrackEntity that = (TrackEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy
                ? ((HibernateProxy) this)
                        .getHibernateLazyInitializer()
                        .getPersistentClass()
                        .hashCode()
                : getClass().hashCode();
    }
}
