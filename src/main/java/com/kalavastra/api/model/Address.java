package com.kalavastra.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "addresses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "address_id")
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Long addressId;

	/** Owning user (references users.user_id). */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
	@JsonIgnore
	private User user;

	/** Expose just the userId in JSON. */
	@JsonProperty("userId")
	public String getUserId() {
		return user != null ? user.getUserId() : null;
	}

	@Column(nullable = false, length = 100)
	private String name;

	@Column(name = "phone_number", nullable = false, length = 15)
	private String phoneNumber;

	@Column(name = "address_line1", nullable = false, columnDefinition = "TEXT")
	private String addressLine1;

	@Column(name = "address_line2", columnDefinition = "TEXT")
	private String addressLine2;

	@Column(nullable = false, length = 100)
	private String city;

	@Column(nullable = false, length = 100)
	private String state;

	@Column(nullable = false, length = 10)
	private String pincode;

	@Builder.Default
	@Column(name = "is_default", nullable = false)
	private Boolean isDefault = false;

	@Builder.Default
	@Column(name = "is_active", nullable = false)
	private Boolean isActive = true;

	@Column(name = "date_created", updatable = false)
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Instant dateCreated;

	@Column(name = "date_updated")
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
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
