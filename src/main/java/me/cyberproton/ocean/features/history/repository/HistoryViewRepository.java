package me.cyberproton.ocean.features.history.repository;

import com.blazebit.persistence.spring.data.repository.BlazeSpecification;
import com.blazebit.persistence.spring.data.repository.EntityViewRepository;

import me.cyberproton.ocean.features.history.dto.HistoryView;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HistoryViewRepository extends EntityViewRepository<HistoryView, Long> {
    Page<HistoryView> findAll(BlazeSpecification specification, Pageable pageable);
}
