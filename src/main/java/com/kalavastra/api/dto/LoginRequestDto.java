package com.kalavastra.api.dto;

import lombok.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Payload for user login.
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LoginRequestDto {

    @NotBlank @Email
    private String email;

    @NotBlank
    private String password;
}
