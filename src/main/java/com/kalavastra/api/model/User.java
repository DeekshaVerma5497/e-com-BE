package com.kalavastra.api.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import java.time.Instant;

/**
 * Represents an application user.
 */
@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@ToString
public class User {

    /**
     * Auto‑generated primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Business key derived from name (e.g. kala_john_0001).
     */
    @Column(name = "user_id", nullable = false, unique = true, length = 50)
    private String userId;

    /**
     * Full name of the user.
     */
    @Column(nullable = false, length = 100)
    private String name;

    /**
     * Email used to login; also {@code username} for Spring Security.
     */
    @Column(nullable = false, unique = true, length = 150)
    private String email;

    /**
     * BCrypt‑hashed password.
     */
    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    /**
     * Optional phone number.
     */
    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    /**
     * Soft‑delete / active flag.
     * 
     * @Builder.Default tells Lombok’s builder to use this default value 
     * unless explicitly overridden in the builder call.
     */
    @Builder.Default
    @Column(name = "is_active")
    private Boolean isActive = true;

    /**
     * Automatically set when row is inserted.
     */
    @CreatedDate
    @Column(name = "date_created", updatable = false)
    private Instant dateCreated;

    /**
     * Automatically updated when row is updated.
     */
    @LastModifiedDate
    @Column(name = "date_updated")
    private Instant dateUpdated;
}
