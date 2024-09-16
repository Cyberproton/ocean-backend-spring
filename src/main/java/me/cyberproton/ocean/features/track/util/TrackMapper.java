package me.cyberproton.ocean.features.track.util;

import me.cyberproton.ocean.features.album.util.AlbumMapper;
import me.cyberproton.ocean.features.artist.ArtistMapper;
import me.cyberproton.ocean.features.track.dto.TrackResponse;
import me.cyberproton.ocean.features.track.entity.TrackDocument;
import me.cyberproton.ocean.features.track.entity.TrackEntity;
import me.cyberproton.ocean.features.track.view.TrackView;
import me.cyberproton.ocean.mapper.MapStructUtils;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        uses = {MapStructUtils.class, ArtistMapper.class, AlbumMapper.class},
        componentModel = "spring")
public interface TrackMapper {
    TrackResponse entityToResponse(TrackEntity entity);

    TrackResponse viewToResponse(TrackView view);

    @Mapping(target = "album", ignore = true)
    @Mapping(target = "artists", ignore = true)
    TrackResponse documentToResponse(TrackDocument document);
}
