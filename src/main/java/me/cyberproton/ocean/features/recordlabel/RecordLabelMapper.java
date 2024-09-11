package me.cyberproton.ocean.features.recordlabel;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RecordLabelMapper {
    RecordLabelDocument entityToDocument(RecordLabelEntity entity);
}
