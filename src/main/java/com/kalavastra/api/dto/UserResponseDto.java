package com.kalavastra.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

/**
 * DTO returned to clients when fetching user profile.
 * Contains only the fields safe for public exposure.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserResponseDto {

    /**
     * Business‑level ID (e.g. kala_amy_1234).
     */
    private String userId;

    /**
     * Full name of the user.
     */
    private String name;

    /**
     * Email address of the user.
     */
    private String email;

    /**
     * Contact phone number.
     */
    private String phoneNumber;

    /**
     * Timestamp when the account was created.
     */
    private Instant dateCreated;

    /**
     * Timestamp when the account was last updated.
     */
    private Instant dateUpdated;

    /**
     * Whether the user account is active.
     */
    private Boolean isActive;
}
