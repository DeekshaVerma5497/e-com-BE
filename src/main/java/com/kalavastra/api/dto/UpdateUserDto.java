package com.kalavastra.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Payload for updating a user’s profile.
 * Only the fields allowed to change are exposed here.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserDto {

    /** User’s full name (required). */
    @NotBlank
    @Size(max = 100)
    private String name;

    /** Optional contact number. */
    @Size(max = 15)
    private String phoneNumber;
}
