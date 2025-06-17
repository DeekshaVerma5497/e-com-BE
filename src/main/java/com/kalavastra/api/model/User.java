package com.kalavastra.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.NotBlank;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_id", nullable = false, unique = true, length = 50)
	private String userId;

	@Column(nullable = false, length = 100)
	private String name;

	@Column(nullable = false, unique = true, length = 100)
	private String email;

	@JsonIgnore
	@NotBlank(message = "Password is required") // never serialize the hash out
	@Column(name = "password_hash", nullable = false)
	private String passwordHash;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@Column(name = "date_of_birth")
	private LocalDate dateOfBirth;

	@Transient
	@JsonProperty(value = "password", access = JsonProperty.Access.WRITE_ONLY)
	private String password;

	@Column(name = "phone_number", length = 20)
	private String phoneNumber;

	@Builder.Default
	@Column(name = "is_active", nullable = false)
	private Boolean isActive = true;

	@Column(name = "date_created", updatable = false)
	private Instant dateCreated;

	@Column(name = "date_updated")
	private Instant dateUpdated;

	@PrePersist
	protected void onCreate() {
		Instant now = Instant.now();
		dateCreated = now;
		dateUpdated = now;
	}

	@PreUpdate
	protected void onUpdate() {
		dateUpdated = Instant.now();
	}
}
