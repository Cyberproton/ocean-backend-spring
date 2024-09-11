package me.cyberproton.ocean.features.file;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileResponse {
    private Long id;
    private String name;
    private String mimetype;
    private String path;
    private Long size;
    private Long owner;
    private boolean isPublic;
    private Date createdAt;

    private Integer width;
    private Integer height;
}
