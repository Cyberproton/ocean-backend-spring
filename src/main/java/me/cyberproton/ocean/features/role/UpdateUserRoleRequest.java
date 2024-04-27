package me.cyberproton.ocean.features.role;

import lombok.Builder;

import java.util.List;

@Builder
public record UpdateUserRoleRequest(List<String> roles) {
}
