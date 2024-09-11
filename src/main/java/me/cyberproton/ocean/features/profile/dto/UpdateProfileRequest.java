package me.cyberproton.ocean.features.profile.dto;

import jakarta.validation.constraints.Size;

import lombok.Builder;

@Builder
public record UpdateProfileRequest(@Size(max = 100) String name, @Size(max = 300) String bio) {}
