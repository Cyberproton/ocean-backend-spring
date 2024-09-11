package me.cyberproton.ocean.repository;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.List;
import java.util.stream.StreamSupport;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;

public abstract class AbstractElasticRepository<E, D> {
    protected final Class<E> entityClass;
    protected final Class<D> documentClass;
    protected final String indexName;
    protected final ElasticsearchOperations elasticsearchOperations;

    public AbstractElasticRepository(
            Class<E> entityClass,
            Class<D> documentClass,
            ElasticsearchOperations elasticsearchOperations) {
        this.entityClass = entityClass;
        this.documentClass = documentClass;
        this.elasticsearchOperations = elasticsearchOperations;
        this.indexName = documentClass.getAnnotation(Document.class).indexName();
    }

    public void saveAll(Iterable<E> entities) {
        List<D> documents =
                StreamSupport.stream(entities.spliterator(), false)
                        .map(this::entityToDocument)
                        .toList();
        elasticsearchOperations.save(documents, IndexCoordinates.of(indexName));
    }

    public void save(E entity) {
        elasticsearchOperations.save(entityToDocument(entity));
    }

    public void delete(E entity) {
        String entityId = getEntityId(entity);
        if (entityId != null) {
            elasticsearchOperations.delete(entityId, documentClass);
            return;
        }
        elasticsearchOperations.delete(entityToDocument(entity));
    }

    @Nonnull
    protected abstract D entityToDocument(E entity);

    @Nullable protected abstract String getEntityId(E entity);
}
