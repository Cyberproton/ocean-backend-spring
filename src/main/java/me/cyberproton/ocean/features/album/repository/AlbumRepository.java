package me.cyberproton.ocean.features.album.repository;


import me.cyberproton.ocean.features.album.entity.AlbumEntity;
import me.cyberproton.ocean.features.user.UserEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepository extends JpaRepository<AlbumEntity, Long> {
    //    LEFT JOIN FETCH a.covers c
    //    LEFT JOIN FETCH a.recordLabel rl
    //    LEFT JOIN FETCH a.copyrights cr
    //    LEFT JOIN FETCH a.tracks t
    //    LEFT JOIN FETCH t.artists artists
    //    LEFT JOIN FETCH artists.user u
    //    LEFT JOIN FETCH profile p WHERE a.id
    @Query(
            value =
                    """
                    SELECT a FROM album a
                    WHERE a.id
                    IN
                        (
                            SELECT id
                            FROM (
                                SELECT id as id, dense_rank() OVER (ORDER BY e.id ASC) as rank
                                FROM album e
                                WHERE :user MEMBER OF e.savedUsers
                           ) WHERE rank BETWEEN :#{#pageable.offset} AND :#{#pageable.offset + #pageable.pageSize}
                    )
                    """,
            countQuery = "SELECT COUNT(e) FROM album e WHERE :user MEMBER OF e.savedUsers")
    Page<AlbumEntity> findAllBySavedUsersContains(
            UserEntity user, Pageable pageable);
}
