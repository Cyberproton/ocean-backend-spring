package me.cyberproton.ocean.features.user;

import jakarta.persistence.*;

import lombok.*;

import me.cyberproton.ocean.features.artist.ArtistEntity;
import me.cyberproton.ocean.features.history.entity.HistoryEntity;
import me.cyberproton.ocean.features.playlist.entity.PlaylistEntity;
import me.cyberproton.ocean.features.profile.entity.ProfileEntity;
import me.cyberproton.ocean.features.role.Role;
import me.cyberproton.ocean.features.track.entity.TrackEntity;

import org.hibernate.proxy.HibernateProxy;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

@Entity(name = "_user") // user is a reserved keyword in Postgresql
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(UserEntityListener.class)
public class UserEntity {
    @Id @GeneratedValue private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    @Column(unique = true)
    private String email;

    private boolean isLocked;

    private boolean isEmailVerified;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles;

    @OneToOne(mappedBy = "user")
    private ProfileEntity profile;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<UserEntity> following;

    @ManyToMany(mappedBy = "following", fetch = FetchType.LAZY)
    private Set<UserEntity> followers;

    @OneToMany private Set<PlaylistEntity> playlists;

    @OneToOne(mappedBy = "user")
    private ArtistEntity artist;

    @OneToMany(mappedBy = "user")
    private Set<HistoryEntity> histories;

    @ManyToMany(mappedBy = "likedUsers")
    private Set<TrackEntity> likedTracks;

    @ManyToMany(mappedBy = "savedUsers")
    private Set<PlaylistEntity> savedPlaylists;

    public void addRole(Role role) {
        Set<Role> roles = getRoles();
        if (roles == null) {
            roles = Set.of(role);
        } else {
            roles.add(role);
        }
        setRoles(roles);
        role.getUsers().add(this);
    }

    public void addAllRoles(Collection<Role> roles) {
        Set<Role> existingRoles = getRoles();
        if (existingRoles == null) {
            existingRoles = Set.copyOf(roles);
        } else {
            existingRoles.addAll(roles);
        }
        setRoles(existingRoles);
        roles.forEach(role -> role.getUsers().add(this));
    }

    public void removeRole(Role role) {
        if (getRoles() == null) {
            return;
        }
        getRoles().remove(role);
        role.getUsers().remove(this);
    }

    public void removeAllRoles(Collection<Role> roles) {
        if (getRoles() == null) {
            return;
        }
        getRoles().removeAll(roles);
        roles.forEach(role -> role.getUsers().remove(this));
    }

    public void addFollowing(UserEntity user) {
        if (getFollowing() == null) {
            setFollowing(Set.of(user));
        } else {
            getFollowing().add(user);
        }
        if (user.getFollowers() == null) {
            user.setFollowers(Set.of(this));
        } else {
            user.getFollowers().add(this);
        }
    }

    public void removeFollowing(UserEntity user) {
        if (getFollowing() == null) {
            return;
        }
        getFollowing().remove(user);
        if (user.getFollowers() != null) {
            user.getFollowers().remove(this);
        }
    }

    public void addLikedTrack(TrackEntity track) {
        if (likedTracks == null) {
            likedTracks = Set.of(track);
        } else {
            likedTracks.add(track);
        }
        if (track.getLikedUsers() == null) {
            track.setLikedUsers(Set.of(this));
        } else {
            track.getLikedUsers().add(this);
        }
    }

    public void removeLikedTrack(TrackEntity track) {
        if (likedTracks == null) {
            return;
        }
        likedTracks.remove(track);
        if (track.getLikedUsers() != null) {
            track.getLikedUsers().remove(this);
        }
    }

    public void addSavedPlaylist(PlaylistEntity playlist) {
        if (savedPlaylists == null) {
            savedPlaylists = Set.of(playlist);
        } else {
            savedPlaylists.add(playlist);
        }
        if (playlist.getSavedUsers() == null) {
            playlist.setSavedUsers(Set.of(this));
        } else {
            playlist.getSavedUsers().add(this);
        }
    }

    public void removeSavedPlaylist(PlaylistEntity playlist) {
        if (savedPlaylists == null) {
            return;
        }
        savedPlaylists.remove(playlist);
        if (playlist.getSavedUsers() != null) {
            playlist.getSavedUsers().remove(this);
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
        UserEntity user = (UserEntity) o;
        return getId() != null && Objects.equals(getId(), user.getId());
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
