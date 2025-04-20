package com.kalavastra.api.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Payload for user signup.
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SignupRequestDto {

    @NotBlank @Size(max = 100)
    private String name;

    @NotBlank @Email @Size(max = 150)
    private String email;

    @NotBlank @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @Size(max = 15)
    private String phoneNumber;
}
