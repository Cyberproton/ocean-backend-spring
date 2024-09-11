package me.cyberproton.ocean.features.copyright;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CopyrightMapper {
    CopyrightDocument entityToDocument(CopyrightEntity entity);
}
