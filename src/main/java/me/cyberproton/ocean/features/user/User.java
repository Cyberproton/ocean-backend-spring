package me.cyberproton.ocean.features.user;

import jakarta.persistence.*;
import lombok.*;
import me.cyberproton.ocean.features.role.Role;

import java.util.Set;

@Entity(name = "_user") // user is a reserved keyword in PostgreSQL
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue
    private Long id;


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
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<User> following;

    @ManyToMany(mappedBy = "following", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<User> followers;

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

    public void addFollowing(User user) {
        getFollowing().add(user);
        user.getFollowers().add(this);
    }

    public void removeFollowing(User user) {
        getFollowing().remove(user);
        user.getFollowers().remove(this);
    }
}
