package me.cyberproton.ocean.features.recordlabel;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;

@EntityView(RecordLabelEntity.class)
public interface RecordLabelView {
    @IdMapping
    Long getId();

    String getName();
}
