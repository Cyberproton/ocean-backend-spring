package me.cyberproton.ocean.features.artist;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;

@EntityView(ArtistEntity.class)
public interface ArtistView {
    @IdMapping
    Long getId();
}
