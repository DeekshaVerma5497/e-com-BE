package com.kalavastra.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Review {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "review_id")
	private Long reviewId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", referencedColumnName = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private Product product;

	@Column(nullable = false)
	private Integer rating;

	@Column(columnDefinition = "TEXT")
	private String comment;

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
