package me.cyberproton.ocean.features.track.repository;

import me.cyberproton.ocean.features.track.entity.TrackDocument;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface TrackElasticsearchRepository extends ElasticsearchRepository<TrackDocument, Long> {
    Page<TrackDocument> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<TrackDocument> findTopByOrderByPopularity(Pageable pageable);
}
