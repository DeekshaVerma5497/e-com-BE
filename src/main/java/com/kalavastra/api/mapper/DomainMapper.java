package com.kalavastra.api.mapper;

import com.kalavastra.api.dto.UpdateUserDto;
import com.kalavastra.api.dto.UserResponseDto;
import com.kalavastra.api.model.User;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

/**
 * MapStruct mapper for converting between Entities and DTOs.
 * Generates code at compile‑time to avoid boilerplate.
 */
@Mapper(componentModel = "spring")
public interface DomainMapper {

    /**
     * Map a User entity to its read‑only response DTO.
     */
    UserResponseDto userToUserResponseDto(User user);

    /**
     * Copy over fields from an UpdateUserDto into an existing User entity.
     */
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    void updateUserFromDto(UpdateUserDto dto, @MappingTarget User user);

}
