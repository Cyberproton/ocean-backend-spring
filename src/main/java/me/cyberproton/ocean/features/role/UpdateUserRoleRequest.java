package me.cyberproton.ocean.features.role;

import java.util.List;
import lombok.Builder;

@Builder
public record UpdateUserRoleRequest(List<String> roles) {}
