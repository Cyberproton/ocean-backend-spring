package me.cyberproton.ocean.repository;

import com.cosium.spring.data.jpa.entity.graph.repository.support.EntityGraphJpaRepositoryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.repository.core.RepositoryMetadata;

public class AppRepositoryFactory extends EntityGraphJpaRepositoryFactory {
    public AppRepositoryFactory(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
        return AppSimpleRepository.class;
    }
}
