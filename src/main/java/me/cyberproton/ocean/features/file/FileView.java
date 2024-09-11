package me.cyberproton.ocean.features.file;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;

@EntityView(FileEntity.class)
public interface FileView {
    @IdMapping
    Long getId();

    Integer getWidth();

    Integer getHeight();
}
