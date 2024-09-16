package me.cyberproton.ocean.features.profile.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import me.cyberproton.ocean.features.file.ImageResponse;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileResponse {
    private Long id;
    private String username;
    private String name;
    private String bio;
    private List<ImageResponse> avatars;
    private List<ImageResponse> banners;
    private Long numberOfFollowers;
    private Long numberOfFollowings;
}
