package me.cyberproton.ocean.features.track.repository;

import me.cyberproton.ocean.features.track.entity.TrackEntity;
import me.cyberproton.ocean.features.user.UserEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface TrackRepository extends JpaRepository<TrackEntity, Long> {
    @Query("SELECT t FROM track t")
    @EntityGraph(value = "eager-track")
    Page<TrackEntity> findAllEagerly(Pageable pageable);

    @EntityGraph(value = "eager-track")
    Page<TrackEntity> findAllByIdIn(Set<Long> ids, Pageable pageable);

    Page<TrackEntity> findAllByNameContainsIgnoreCase(String name, Pageable pageable);

    Page<TrackEntity> findAllByPlaylistTracksPlaylistIdOrderByPlaylistTracksTrackPosition(
            Long playlistId, Pageable pageable);

    Page<TrackEntity> findAllByLikedUsersIdContains(UserEntity user, Pageable pageable);
}
