package me.cyberproton.ocean.features.history.repository;

import me.cyberproton.ocean.features.history.dto.HistoryQuery;
import me.cyberproton.ocean.features.history.entity.HistoryEntity;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HistoryRepository extends JpaRepository<HistoryEntity, Long> {
    Optional<HistoryEntity>
            findFirstByUserIdAndTrackIdAndAlbumIdAndArtistIdAndPlaylistIdOrderByUpdatedAtDesc(
                    Long userId, Long trackId, Long albumId, Long artistId, Long playlistId);

    @Query(
            value =
                    """
                    SELECT h FROM history h WHERE h.id
                    IN
                        (
                            SELECT id
                            FROM (
                                SELECT id as id, dense_rank() OVER (ORDER BY h.id ASC) as rank
                                FROM history h
                                WHERE h.user.id = :userId AND (:#{#query.type} IS NULL OR h.type = :#{#query.type})
                           ) WHERE rank BETWEEN :#{#pageable.offset} AND :#{#pageable.offset + #pageable.pageSize}
                    )
                    """,
            countQuery =
                    "SELECT COUNT(h) FROM history h WHERE h.user.id = :userId AND (:#{#query.type} IS NULL OR h.type = :#{#query.type})")
    List<HistoryEntity> findAllEfficientPagination(
            Pageable pageable, @Param("userId") Long userId, @Param("query") HistoryQuery query);

    Long countByTrackId(Long trackId);
}
