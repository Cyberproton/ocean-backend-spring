package me.cyberproton.ocean.features.file;

import jakarta.persistence.*;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.cyberproton.ocean.features.user.UserEntity;
import org.hibernate.annotations.CreationTimestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "file")
public class FileEntity {
    @Id @GeneratedValue private Long id;

    @Builder.Default @Enumerated private FileType type = FileType.OTHER;

    private String name;

    private String mimetype;

    private String path;

    private Long size;

    private boolean isPublic;

    @ManyToOne private UserEntity owner;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Date createdAt;

    // Image specific fields

    private Integer width;

    private Integer height;
}
