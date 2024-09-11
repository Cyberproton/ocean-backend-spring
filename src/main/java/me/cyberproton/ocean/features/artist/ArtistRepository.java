package me.cyberproton.ocean.features.artist;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArtistRepository extends JpaRepository<ArtistEntity, Long> {
    @EntityGraph(attributePaths = "user.profile")
    Optional<ArtistEntity> findWithProfileById(Long id);

    Optional<ArtistEntity> findFirstByUserId(Long userId);

    Optional<ArtistEntity> findFirstByUserProfileId(Long profileId);
}
