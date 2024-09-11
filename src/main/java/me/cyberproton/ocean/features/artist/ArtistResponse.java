package me.cyberproton.ocean.features.artist;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ArtistResponse {
    private Long id;
    private String username;
    private String name;
    private String bio;
    private String avatarUrl;
    private String bannerUrl;

    public static ArtistResponse fromEntity(ArtistEntity artist) {
        return ArtistResponse.builder().id(artist.getId()).build();
    }

    public static ArtistResponse fromElasticsearchDocument(ArtistDocument artistDocument) {
        return ArtistResponse.builder().id(artistDocument.getId()).build();
    }
}
