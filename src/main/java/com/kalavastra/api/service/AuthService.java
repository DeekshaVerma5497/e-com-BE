package com.kalavastra.api.service;

import com.kalavastra.api.dto.AuthResponseDto;
import com.kalavastra.api.dto.LoginRequestDto;
import com.kalavastra.api.dto.SignupRequestDto;
import com.kalavastra.api.model.User;
import com.kalavastra.api.repository.UserRepository;
import com.kalavastra.api.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static org.springframework.security.core.userdetails.User.*;

/**
 * Handles user registration and login, and issues JWT tokens.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepo;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /**
     * Registers a new user and returns a JWT token.
     *
     * @param dto signup request with name, email, password
     * @return AuthResponseDto with token and user info
     */
    public AuthResponseDto signup(SignupRequestDto dto) {
        userRepo.findByEmail(dto.getEmail())
                .ifPresent(u -> {
                    throw new IllegalArgumentException("Email already in use");
                });

        String userId = "kala_" +
                dto.getName().toLowerCase().replaceAll("\\s+", "") + "_" +
                UUID.randomUUID().toString().substring(0, 5);

        User user = User.builder()
                .userId(userId)
                .name(dto.getName())
                .email(dto.getEmail())
                .passwordHash(passwordEncoder.encode(dto.getPassword()))
                .phoneNumber(dto.getPhoneNumber())
                .build();

        user = userRepo.save(user);

        String token = jwtUtil.generateToken(user.getEmail());

        return AuthResponseDto.builder()
                .token(token)
                .tokenType("Bearer")
                .userId(user.getUserId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    /**
     * Authenticates user credentials and returns a JWT token.
     *
     * @param dto login request with email and password
     * @return AuthResponseDto with token and user info
     */
    public AuthResponseDto login(LoginRequestDto dto) {
        User user = userRepo.findByEmail(dto.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(user.getEmail());

        return AuthResponseDto.builder()
                .token(token)
                .tokenType("Bearer")
                .userId(user.getUserId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    /**
     * Builds a Spring Security UserDetails object from the User entity.
     * Required for consistent JWT generation & validation.
     *
     * @param user our User entity
     * @return Spring Security UserDetails
     */
    private UserDetails buildUserDetails(User user) {
        return withUsername(user.getEmail())
                .password(user.getPasswordHash())
                .authorities("ROLE_USER")
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!user.getIsActive())
                .build();
    }
}
