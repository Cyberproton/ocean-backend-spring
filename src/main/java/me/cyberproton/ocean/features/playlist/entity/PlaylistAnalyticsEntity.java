package me.cyberproton.ocean.features.playlist.entity;

import jakarta.persistence.*;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity(name = "playlist_analytics")
public class PlaylistAnalyticsEntity {
    @Id @GeneratedValue private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private PlaylistEntity playlist;

    @Builder.Default
    @Column(nullable = false, columnDefinition = "bigint default 0")
    private Long numberOfPlays = 0L;

    @Builder.Default
    @Column(nullable = false, columnDefinition = "bigint default 0")
    private Long playTotalTimestampInMinutes = 0L;

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
