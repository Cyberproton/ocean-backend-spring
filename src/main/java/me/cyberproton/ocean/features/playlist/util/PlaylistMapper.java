package me.cyberproton.ocean.features.playlist.util;

import me.cyberproton.ocean.features.file.FileMapper;
import me.cyberproton.ocean.features.playlist.dto.PlaylistResponse;
import me.cyberproton.ocean.features.playlist.entity.PlaylistDocument;
import me.cyberproton.ocean.features.playlist.entity.PlaylistEntity;
import me.cyberproton.ocean.features.playlist.repository.PlaylistTrackRepositoryForMapper;
import me.cyberproton.ocean.features.playlist.view.PlaylistView;
import me.cyberproton.ocean.util.PersistenceUtils;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = FileMapper.class)
public abstract class PlaylistMapper {
    @Autowired private PlaylistTrackRepositoryForMapper playlistTrackRepositoryForMapper;

    @Mapping(target = "totalTracks", expression = "java(totalTracks(playlistEntity))")
    public abstract PlaylistResponse entityToResponse(PlaylistEntity playlistEntity);

    public abstract PlaylistResponse viewToResponse(PlaylistView input);

    public abstract PlaylistResponse documentToResponse(PlaylistDocument source);

    public long totalTracks(PlaylistEntity playlistEntity) {
        if (PersistenceUtils.isLoaded(playlistEntity.getPlaylistTracks())) {
            return playlistEntity.getPlaylistTracks().size();
        }
        return playlistTrackRepositoryForMapper.countByPlaylistId(playlistEntity.getId());
    }
}
