package me.cyberproton.ocean.features.track.repository;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;

import jakarta.persistence.EntityManager;

import lombok.AllArgsConstructor;

import me.cyberproton.ocean.domain.BaseQuery;
import me.cyberproton.ocean.features.track.entity.TrackEntity;
import me.cyberproton.ocean.features.track.entity.TrackEntity_;
import me.cyberproton.ocean.features.track.view.TrackView;
import me.cyberproton.ocean.features.user.UserEntity;

@AllArgsConstructor
public class TrackViewRepositoryExtensionImpl implements TrackViewRepositoryExtension {
    private final EntityManager entityManager;
    private final CriteriaBuilderFactory criteriaBuilderFactory;
    private final EntityViewManager entityViewManager;

    @Override
    public PagedList<TrackView> findAllByLikedUsersContains(UserEntity user, BaseQuery query) {
        return entityViewManager
                .applySetting(
                        EntityViewSetting.create(
                                TrackView.class, query.getOffset(), query.getLimit()),
                        criteriaBuilderFactory.create(entityManager, TrackEntity.class))
                .where(":user")
                .isMemberOf(TrackEntity_.LIKED_USERS)
                .orderByAsc("id")
                .setParameter("user", user)
                .getResultList();
    }
}
