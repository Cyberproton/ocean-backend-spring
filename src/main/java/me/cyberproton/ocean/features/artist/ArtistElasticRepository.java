package me.cyberproton.ocean.features.artist;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import me.cyberproton.ocean.repository.AbstractElasticRepository;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Component;

@Component
public class ArtistElasticRepository
        extends AbstractElasticRepository<ArtistEntity, ArtistDocument> {
    public ArtistElasticRepository(ElasticsearchOperations elasticsearchOperations) {
        super(ArtistEntity.class, ArtistDocument.class, elasticsearchOperations);
    }

    @Nonnull
    @Override
    protected ArtistDocument entityToDocument(ArtistEntity artist) {
        return ArtistDocument.builder().id(artist.getId()).build();
    }

    @Nullable @Override
    protected String getEntityId(ArtistEntity entity) {
        return entity.getId().toString();
    }
}
