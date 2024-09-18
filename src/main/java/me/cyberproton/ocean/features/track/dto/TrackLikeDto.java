package me.cyberproton.ocean.features.track.dto;

public record TrackLikeDto(
        Long trackId, Long numberOfLikes, Long userId, Long userLikedAtTimestamp, LikeType type) {
    public enum LikeType {
        LIKE,
        UNLIKE
    }
}
