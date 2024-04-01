package me.cyberproton.ocean.features.user;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = {"following", "followers"})
    Optional<User> findEagerById(Long id);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameOrEmail(String username, String email);

    Optional<Set<User>> findFollowingByFollowersId(Long id);
}
