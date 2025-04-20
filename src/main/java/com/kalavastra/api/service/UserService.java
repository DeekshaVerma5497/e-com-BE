package com.kalavastra.api.service;

import com.kalavastra.api.dto.UpdateUserDto;
import com.kalavastra.api.dto.UserResponseDto;
import com.kalavastra.api.exception.ResourceNotFoundException;
import com.kalavastra.api.mapper.DomainMapper;
import com.kalavastra.api.model.User;
import com.kalavastra.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Business logic for user profiles (get & update).
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepo;
    private final DomainMapper mapper;

    public UserResponseDto getByUserId(String userId) {
        User u = userRepo.findByUserId(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
        System.out.println(">>> ENTITY: " + u);
        UserResponseDto dto = mapper.userToUserResponseDto(u);
        System.out.println(">>> MAPPED DTO: " + dto);
        return dto;
//        return mapper.userToUserResponseDto(u);
    }

    public UserResponseDto update(String userId, UpdateUserDto dto) {
        User u = userRepo.findByUserId(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
        u.setName(dto.getName());
        u.setPhoneNumber(dto.getPhoneNumber());
        return mapper.userToUserResponseDto(userRepo.save(u));
    }
}
