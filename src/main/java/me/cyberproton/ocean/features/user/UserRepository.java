package me.cyberproton.ocean.features.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @EntityGraph(attributePaths = {"following", "followers"})
    Optional<UserEntity> findEagerById(Long id);

    @EntityGraph(attributePaths = {"roles"})
    List<UserEntity> findAllWithRolesByIdInAndRolesName(Collection<Long> id, String roleName);

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByUsernameOrEmail(String username, String email);

    Optional<Set<UserEntity>> findFollowingByFollowersId(Long id);

    Optional<UserEntity> findByProfileId(Long id);

        Page<UserEntity> findAllByArtistNotNull(Pageable pageRequest);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    Long countByFollowingId(Long id);

    Long countByFollowersId(Long id);
}
