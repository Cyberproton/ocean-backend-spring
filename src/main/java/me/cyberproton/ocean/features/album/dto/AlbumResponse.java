package me.cyberproton.ocean.features.album.dto;

import com.blazebit.persistence.view.IdMapping;

import lombok.*;

import me.cyberproton.ocean.features.album.entity.AlbumType;
import me.cyberproton.ocean.features.copyright.CopyrightResponse;
import me.cyberproton.ocean.features.file.ImageResponse;
import me.cyberproton.ocean.features.profile.dto.ProfileResponse;
import me.cyberproton.ocean.features.recordlabel.RecordLabelResponse;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlbumResponse {
    @IdMapping private Long id;
    private AlbumType type;
    private String name;
    private String description;
    private Date releaseDate;
    private List<ImageResponse> covers;
    private RecordLabelResponse recordLabel;
    private Set<CopyrightResponse> copyrights;
    private List<ProfileResponse> artists;
    private Long popularity;
}
