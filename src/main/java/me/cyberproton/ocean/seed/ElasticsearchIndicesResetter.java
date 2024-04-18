package me.cyberproton.ocean.seed;

import lombok.AllArgsConstructor;
import me.cyberproton.ocean.listener.ElasticIndicesCreator;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class ElasticsearchIndicesResetter {
    private final ElasticIndicesCreator elasticIndicesCreator;

    public void reset() {
        elasticIndicesCreator.deleteDefaultIndices();
        elasticIndicesCreator.createDefaultIndices();
    }
}
