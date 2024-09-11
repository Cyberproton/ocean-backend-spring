package me.cyberproton.ocean.features.copyright;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;

@EntityView(CopyrightEntity.class)
public interface CopyrightView {
    @IdMapping
    Long getId();

    String getText();

    CopyrightType getType();
}
