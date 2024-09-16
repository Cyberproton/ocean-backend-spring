package me.cyberproton.ocean.features.elasticsearch;

import me.cyberproton.ocean.features.track.entity.TrackDocument;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface TestSearchRepository extends ElasticsearchRepository<TrackDocument, Long> {}
