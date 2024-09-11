package me.cyberproton.ocean.features.album.repository;

import com.blazebit.persistence.spring.data.repository.EntityViewRepository;

import me.cyberproton.ocean.features.album.dto.AlbumView;

public interface AlbumViewRepository extends EntityViewRepository<AlbumView, Long> {}
