package me.cyberproton.ocean.features.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepositoryForMapper extends JpaRepository<UserEntity, Long> {
    Long countByFollowingId(Long id);

    Long countByFollowersId(Long id);
}
