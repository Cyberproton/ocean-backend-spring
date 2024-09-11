package me.cyberproton.ocean.features.email;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
@AllArgsConstructor
public class EmailRequest {
    @NonNull private String to;
    @NonNull private String subject;
    @NonNull private String content;
}
