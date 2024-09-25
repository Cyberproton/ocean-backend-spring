package me.cyberproton.ocean.features.album.service;

import lombok.AllArgsConstructor;

import me.cyberproton.ocean.domain.BaseQuery;
import me.cyberproton.ocean.features.album.dto.AlbumResponse;
import me.cyberproton.ocean.features.album.dto.CreateOrUpdateAlbumRequest;
import me.cyberproton.ocean.features.album.entity.AlbumEntity;
import me.cyberproton.ocean.features.album.event.AlbumEvent;
import me.cyberproton.ocean.features.album.repository.AlbumRepository;
import me.cyberproton.ocean.features.album.repository.AlbumViewRepository;
import me.cyberproton.ocean.features.album.util.AlbumMapper;
import me.cyberproton.ocean.features.copyright.CopyrightEntity;
import me.cyberproton.ocean.features.copyright.CopyrightRepository;
import me.cyberproton.ocean.features.recordlabel.RecordLabelEntity;
import me.cyberproton.ocean.features.recordlabel.RecordLabelRepository;
import me.cyberproton.ocean.util.ImageUrlMapper;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class AlbumService {
    private final AlbumRepository albumRepository;
    private final RecordLabelRepository recordLabelRepository;
    private final CopyrightRepository copyrightRepository;
    private final ImageUrlMapper imageUrlMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final AlbumMapper albumMapper;
    private final AlbumViewRepository albumViewRepository;

    public Set<AlbumResponse> getAlbums() {
        return albumRepository.findAll().stream()
                .map(albumMapper::entityToResponse)
                .collect(Collectors.toSet());
    }

    public AlbumResponse getAlbum(Long id) {
        AlbumEntity album =
                albumRepository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new ResponseStatusException(
                                                HttpStatus.NOT_FOUND, "Album not found"));
        return albumMapper.entityToResponse(album);
    }

    public AlbumResponse createAlbum(CreateOrUpdateAlbumRequest request) {
        RecordLabelEntity recordLabel =
                recordLabelRepository.findById(request.recordLabelId()).orElseThrow();
        List<CopyrightEntity> copyrights = copyrightRepository.findAllById(request.copyrightIds());
        if (copyrights.size() != request.copyrightIds().size()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Some of the copyrights are not found");
        }
        AlbumEntity album =
                AlbumEntity.builder()
                        .type(request.type())
                        .name(request.name())
                        .description(request.description())
                        .releaseDate(request.releaseDate())
                        .recordLabel(recordLabel)
                        .copyrights(Set.copyOf(copyrights))
                        .build();
        AlbumEntity saved = albumRepository.save(album);
        eventPublisher.publishEvent(new AlbumEvent(AlbumEvent.Type.CREATE, saved));
        return albumMapper.entityToResponse(saved);
    }

    public AlbumResponse updateAlbum(Long id, CreateOrUpdateAlbumRequest request) {
        RecordLabelEntity recordLabel =
                recordLabelRepository.findById(request.recordLabelId()).orElseThrow();
        List<CopyrightEntity> copyrights = copyrightRepository.findAllById(request.copyrightIds());
        if (copyrights.size() != request.copyrightIds().size()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Some of the copyrights are not found");
        }
        AlbumEntity album = albumRepository.findById(id).orElseThrow();
        album.setType(request.type());
        album.setName(request.name());
        album.setDescription(request.description());
        album.setReleaseDate(request.releaseDate());
        album.setRecordLabel(recordLabel);
        album.setCopyrights(Set.copyOf(copyrights));
        AlbumEntity saved = albumRepository.save(album);
        eventPublisher.publishEvent(new AlbumEvent(AlbumEvent.Type.UPDATE, saved));
        return albumMapper.entityToResponse(saved);
    }

    public void deleteAlbum(Long id) {
        AlbumEntity album = albumRepository.findById(id).orElseThrow();
        albumRepository.delete(album);
        eventPublisher.publishEvent(new AlbumEvent(AlbumEvent.Type.DELETE, album));
    }

    public Page<AlbumResponse> getTopAlbums(BaseQuery query) {
        Pageable pageable =
                query.toOffsetBasedPageable(
                        Sort.by(Sort.Order.desc("popularity"), Sort.Order.asc("id")));
        return albumViewRepository.findAll(pageable).map(albumMapper::viewToResponse);
    }
}
