package com.kalavastra.api.dto;

import lombok.*;

/**
 * Returned after successful login/signup.
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AuthResponseDto {
    private String token;       // JWT
    private String tokenType;   // e.g. "Bearer"
    private String userId;
    private String email;
    private String name;
}
