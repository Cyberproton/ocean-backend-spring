package me.cyberproton.ocean.features.file;

import me.cyberproton.ocean.mapper.MapStructUtils;
import me.cyberproton.ocean.util.ImageUrlMapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring", uses = MapStructUtils.class)
public abstract class FileMapper {
    @Autowired private ImageUrlMapper imageMapper;

    @Mapping(target = "url", expression = "java(url(fileEntity.getId()))")
    public abstract ImageResponse toResponse(FileEntity fileEntity);

    @Mapping(target = "url", expression = "java(url(fileDocument.getId()))")
    public abstract ImageResponse toResponse(FileDocument fileDocument);

    @Mapping(target = "url", expression = "java(url(input.getId()))")
    public abstract ImageResponse viewToResponse(FileView input);

    public List<ImageResponse> singleToMultipleResponses(FileEntity fileEntity) {
        return List.of(toResponse(fileEntity));
    }

    public List<ImageResponse> singleToMultipleResponses(FileDocument fileDocument) {
        return List.of(toResponse(fileDocument));
    }

    public List<ImageResponse> singleToMultipleResponses(FileView fileView) {
        return List.of(viewToResponse(fileView));
    }

    public FileDocument entityToDocument(FileEntity fileEntity) {
        return FileDocument.builder()
                .id(fileEntity.getId())
                .type(fileEntity.getType())
                .name(fileEntity.getName())
                .mimetype(fileEntity.getMimetype())
                .path(fileEntity.getPath())
                .size(fileEntity.getSize())
                .isPublic(fileEntity.isPublic())
                .width(fileEntity.getWidth())
                .height(fileEntity.getHeight())
                .build();
    }

    public String url(long fileId) {
        return imageMapper.mapFileIdToUrl(fileId);
    }
}
