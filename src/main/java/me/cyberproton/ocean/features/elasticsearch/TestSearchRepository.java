package me.cyberproton.ocean.features.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface TestSearchRepository extends ElasticsearchRepository<TrackDocument, Long> {
}
