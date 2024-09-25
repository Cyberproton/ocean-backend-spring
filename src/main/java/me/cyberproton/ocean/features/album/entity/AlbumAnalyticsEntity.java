package me.cyberproton.ocean.features.album.entity;

import jakarta.persistence.*;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity(name = "album_analytics")
public class AlbumAnalyticsEntity {
    @Id @GeneratedValue private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private AlbumEntity album;

    @Builder.Default
    @Column(nullable = false, columnDefinition = "bigint default 0")
    private Long numberOfTrackPlays = 0L;

    @Builder.Default
    @Column(nullable = false, columnDefinition = "bigint default 0")
    private Long trackPlayTotalTimestampInMinutes = 0L;

    @Builder.Default
    @Column(nullable = false, columnDefinition = "bigint default 0")
    private Long numberOfSaves = 0L;

    @Builder.Default
    @Column(nullable = false, columnDefinition = "bigint default 0")
    private Long saveTotalTimestampInMinutes = 0L;

    @Builder.Default
    @Column(nullable = false, columnDefinition = "bigint default 0")
    private Long popularity = 0L;
}
