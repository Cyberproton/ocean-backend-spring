package me.cyberproton.ocean.features.user;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = {"following", "followers"})
    Optional<User> findEagerById(Long id);

    @EntityGraph(attributePaths = {"roles"})
    List<User> findAllWithRolesByIdInAndRolesName(Collection<Long> id, String roleName);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameOrEmail(String username, String email);

    Optional<Set<User>> findFollowingByFollowersId(Long id);

    Optional<User> findByProfileId(Long id);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    Long countByFollowingId(Long id);

    Long countByFollowersId(Long id);
}
