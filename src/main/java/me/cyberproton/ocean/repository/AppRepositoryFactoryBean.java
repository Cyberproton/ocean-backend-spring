package me.cyberproton.ocean.repository;

import com.cosium.spring.data.jpa.entity.graph.repository.support.EntityGraphJpaRepositoryFactoryBean;
import jakarta.persistence.EntityManager;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

public class AppRepositoryFactoryBean<R extends Repository<T, I>, T, I>
        extends EntityGraphJpaRepositoryFactoryBean<R, T, I> {
    public AppRepositoryFactoryBean(Class<? extends R> repositoryInterface) {
        super(repositoryInterface);
    }

    @Override
    protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
        return new AppRepositoryFactory(entityManager);
    }
}
