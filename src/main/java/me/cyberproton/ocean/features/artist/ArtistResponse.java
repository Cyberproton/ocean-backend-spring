package me.cyberproton.ocean.features.artist;

public record ArtistResponse(Long id) {
    public static ArtistResponse fromEntity(Artist artist) {
        return new ArtistResponse(artist.getId());
    }
}
