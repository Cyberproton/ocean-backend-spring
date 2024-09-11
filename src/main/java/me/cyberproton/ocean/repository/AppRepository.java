package me.cyberproton.ocean.repository;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface AppRepository<T, ID> extends EntityGraphJpaRepository<T, ID> {
    List<ID> findAllIds(Specification<T> spec);

    List<ID> findAllIds();

    Page<T> findAllWithEfficientPagination(Pageable pageable);

    Page<T> findAllWithEfficientPagination(Specification<T> spec, Pageable pageable);

    Page<T> findAllWithEfficientPagination(
            Specification<T> spec, Pageable pageable, EntityGraph entityGraph);
}
