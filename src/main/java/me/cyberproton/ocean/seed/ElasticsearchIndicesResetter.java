package me.cyberproton.ocean.seed;

import lombok.AllArgsConstructor;
import me.cyberproton.ocean.listener.ElasticIndicesCreator;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
@Profile("seeder")
public class ElasticsearchIndicesResetter {
    private final ElasticIndicesCreator elasticIndicesCreator;

    public void reset() {
        elasticIndicesCreator.deleteDefaultIndices();
        elasticIndicesCreator.createDefaultIndices();
    }
}
