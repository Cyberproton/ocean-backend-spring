package me.cyberproton.ocean.features.user;

import me.cyberproton.ocean.mapper.MapStructUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = {MapStructUtils.class})
public interface UserMapper {
    @Mapping(target = "isEmailVerified", source = "emailVerified")
    @Mapping(target = "isLocked", source = "locked")
    UserResponse entityToResponse(UserEntity entity);
}
