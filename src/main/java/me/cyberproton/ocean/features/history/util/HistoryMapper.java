package me.cyberproton.ocean.features.history.util;

import me.cyberproton.ocean.features.album.util.AlbumMapper;
import me.cyberproton.ocean.features.artist.ArtistMapper;
import me.cyberproton.ocean.features.file.FileMapper;
import me.cyberproton.ocean.features.history.dto.HistoryResponse;
import me.cyberproton.ocean.features.history.dto.HistoryView;
import me.cyberproton.ocean.features.history.entity.HistoryEntity;
import me.cyberproton.ocean.features.playlist.util.PlaylistMapper;
import me.cyberproton.ocean.mapper.MapStructConfig;
import me.cyberproton.ocean.mapper.MapStructUtils;

import org.mapstruct.Mapper;

@Mapper(
        config = MapStructConfig.class,
        uses = {
            MapStructUtils.class,
            FileMapper.class,
            ArtistMapper.class,
            PlaylistMapper.class,
            AlbumMapper.class
        },
        componentModel = "spring")
public interface HistoryMapper {
    HistoryResponse entityToResponse(HistoryEntity historyEntity);

    HistoryResponse viewToResponse(HistoryView input);
}
