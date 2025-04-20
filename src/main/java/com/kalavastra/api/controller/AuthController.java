package com.kalavastra.api.controller;

import com.kalavastra.api.dto.AuthResponseDto;
import com.kalavastra.api.dto.LoginRequestDto;
import com.kalavastra.api.dto.SignupRequestDto;
import com.kalavastra.api.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Public endpoints for authentication (signup & login).
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Register a new user")
    @PostMapping("/signup")
    public ResponseEntity<AuthResponseDto> signup(
            @Valid @RequestBody SignupRequestDto dto
    ) {
        return ResponseEntity.ok(authService.signup(dto));
    }

    @Operation(summary = "Authenticate user and return JWT")
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(
            @Valid @RequestBody LoginRequestDto dto
    ) {
        return ResponseEntity.ok(authService.login(dto));
    }
}
