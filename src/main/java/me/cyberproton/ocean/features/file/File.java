package me.cyberproton.ocean.features.file;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.cyberproton.ocean.features.user.User;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class File {
    @Id
    private String id;

    private String name;

    private String mimetype;

    private String path;

    private Long size;

    @ManyToOne
    private User user;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Date createdAt;
}
