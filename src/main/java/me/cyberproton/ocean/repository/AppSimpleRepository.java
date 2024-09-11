package me.cyberproton.ocean.repository;

import static org.springframework.data.jpa.repository.support.JpaEntityInformationSupport.getEntityInformation;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.repository.support.EntityGraphSimpleJpaRepository;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.metamodel.SingularAttribute;
import java.util.Collections;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;

@SuppressWarnings("unchecked")
public class AppSimpleRepository<T, ID> extends EntityGraphSimpleJpaRepository<T, ID>
        implements AppRepository<T, ID> {
    private final EntityManager entityManager;
    private final JpaEntityInformation<T, ?> entityInformation;

    public AppSimpleRepository(
            JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
        this.entityInformation = entityInformation;
    }

    public AppSimpleRepository(Class<T> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);
        this.entityManager = entityManager;
        this.entityInformation = getEntityInformation(domainClass, entityManager);
    }

    @Override
    public List<ID> findAllIds(@Nullable Specification<T> spec) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<?> query = cb.createQuery(entityInformation.getIdType());
        Root<T> root = query.from(getDomainClass());

        SingularAttribute<? super T, ?> idAttribute = entityInformation.getIdAttribute();
        if (idAttribute == null) {
            throw new IllegalStateException("This model has no ID attribute");
        }

        query.select(root.get(idAttribute.getName()));

        if (spec != null) {
            query.where(spec.toPredicate(root, query, cb));
        }

        return (List<ID>) entityManager.createQuery(query).getResultList();
    }

    @Override
    public List<ID> findAllIds() {
        return findAllIds(null);
    }

    public Page<T> findAllWithEfficientPagination(
            Specification<T> spec, Pageable pageable, EntityGraph entityGraph) {
        Class<T> domainClass = getDomainClass();
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        // First, get IDs
        CriteriaQuery<Object> idQuery = cb.createQuery();
        Root<T> idRoot = idQuery.from(domainClass);
        idQuery.select(idRoot.get(entityInformation.getIdAttribute().getName()));

        if (spec != null) {
            idQuery.where(spec.toPredicate(idRoot, idQuery, cb));
        }

        idQuery.orderBy(QueryUtils.toOrders(pageable.getSort(), idRoot, cb));

        TypedQuery<Object> typedIdQuery = entityManager.createQuery(idQuery);
        typedIdQuery.setFirstResult((int) pageable.getOffset());
        typedIdQuery.setMaxResults(pageable.getPageSize());

        List<ID> ids = (List<ID>) typedIdQuery.getResultList();

        System.out.println(ids);

        if (ids.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }

        // Second, get entities by IDs
        CriteriaQuery<T> entityQuery = cb.createQuery(domainClass);
        Root<T> entityRoot = entityQuery.from(domainClass);

        entityQuery =
                entityQuery.where(
                        cb.and(
                                entityRoot.get(entityInformation.getIdAttribute().getName()).in(ids)
                                //                        spec != null ?
                                // spec.toPredicate(entityRoot, entityQuery,
                                // cb) : cb.conjunction()
                                ));

        // Preserve the order
        entityQuery = entityQuery.orderBy(QueryUtils.toOrders(pageable.getSort(), entityRoot, cb));

        TypedQuery<T> typedEntityQuery = entityManager.createQuery(entityQuery);
        if (entityGraph != null) {
            entityGraph
                    .buildQueryHint(entityManager, domainClass)
                    .ifPresent(
                            (c) ->
                                    typedEntityQuery.setHint(
                                            "jakarta.persistence.fetchgraph", c.entityGraph()));
        }

        List<T> entities = typedEntityQuery.getResultList();

        // Count query
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<T> countRoot = countQuery.from(domainClass);
        countQuery.select(cb.count(countRoot));
        if (spec != null) {
            countQuery.where(spec.toPredicate(countRoot, countQuery, cb));
        }
        Long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(entities, pageable, total);
    }

    @Override
    public Page<T> findAllWithEfficientPagination(Specification<T> spec, Pageable pageable) {
        return findAllWithEfficientPagination(spec, pageable, null);
    }

    @Override
    public Page<T> findAllWithEfficientPagination(Pageable pageable) {
        return findAllWithEfficientPagination(null, pageable);
    }
}
