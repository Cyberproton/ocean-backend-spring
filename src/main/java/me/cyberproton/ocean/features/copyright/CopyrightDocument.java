package me.cyberproton.ocean.features.copyright;

import lombok.Data;

@Data
public class CopyrightDocument {
    private Long id;

    private String text;

    private CopyrightType type;
}
