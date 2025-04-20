package com.kalavastra.api.controller;

import com.kalavastra.api.dto.UpdateUserDto;
import com.kalavastra.api.dto.UserResponseDto;
import com.kalavastra.api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Secured endpoints for user profile retrieval & updates.
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @Operation(summary = "Get user profile by userId")
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getProfile(
            @PathVariable String userId
    ) {
        return ResponseEntity.ok(userService.getByUserId(userId));
    }

    @Operation(summary = "Update user profile")
    @PutMapping("/{userId}")
    public ResponseEntity<UserResponseDto> updateProfile(
            @PathVariable String userId,
            @Valid @RequestBody UpdateUserDto dto
    ) {
        return ResponseEntity.ok(userService.update(userId, dto));
    }
}
