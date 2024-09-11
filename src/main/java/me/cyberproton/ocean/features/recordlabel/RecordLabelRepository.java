package me.cyberproton.ocean.features.recordlabel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordLabelRepository extends JpaRepository<RecordLabelEntity, Long> {}
