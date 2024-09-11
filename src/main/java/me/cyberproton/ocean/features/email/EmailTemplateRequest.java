package me.cyberproton.ocean.features.email;

import java.util.Map;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class EmailTemplateRequest {
    @NonNull private String to;
    @NonNull private String subject;
    @NonNull private String template;
    private Map<String, Object> model;
}
