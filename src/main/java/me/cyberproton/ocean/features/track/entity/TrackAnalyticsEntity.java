package me.cyberproton.ocean.features.track.entity;

import jakarta.persistence.*;

import lombok.*;

import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.proxy.HibernateProxy;

import java.util.Date;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity(name = "track_analytics")
public class TrackAnalyticsEntity {
    @Id @GeneratedValue private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private TrackEntity track;

    @Builder.Default
    @Column(nullable = false, columnDefinition = "bigint default 0")
    private Long numberOfPlays = 0L;

    @Builder.Default
    @Column(nullable = false, columnDefinition = "bigint default 0")
    private Long playTotalTimestampInMinutes = 0L;

    @Builder.Default
    @Column(nullable = false, columnDefinition = "bigint default 0")
    private Long numberOfLikes = 0L;

    @Builder.Default
    @Column(nullable = false, columnDefinition = "bigint default 0")
    private Long likeTotalTimestampInMinutes = 0L;

    @Builder.Default
    @Column(nullable = false, columnDefinition = "bigint default 0")
    private Long popularity = 0L;

    @Column(nullable = false)
    @UpdateTimestamp
    private Date updatedAt;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass =
                o instanceof HibernateProxy
                        ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass()
                        : o.getClass();
        Class<?> thisEffectiveClass =
                this instanceof HibernateProxy
                        ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass()
                        : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        TrackAnalyticsEntity that = (TrackAnalyticsEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy
                ? ((HibernateProxy) this)
                        .getHibernateLazyInitializer()
                        .getPersistentClass()
                        .hashCode()
                : getClass().hashCode();
    }
}
