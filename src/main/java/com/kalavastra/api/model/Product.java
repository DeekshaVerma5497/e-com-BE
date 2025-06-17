package com.kalavastra.api.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "product_code", unique = true, nullable = false, length = 50)
	private String productCode;

	@Column(nullable = false, length = 150)
	private String name;
	@Column(length = 500)
	private String description;
	@Column(nullable = false)
	private BigDecimal price;
	@Column(nullable = false)
	private Integer stock;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "extension", columnDefinition = "jsonb")
	private Map<String, Object> extension = new HashMap<>();

	@Column(name = "is_active")
	@Builder.Default
	private Boolean isActive = true;

	@Transient
	@JsonProperty("wishlisted")
	private Boolean wishlisted = false;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_code", referencedColumnName = "category_code", nullable = false)
	@JsonIgnore
	private Category category;

	@Transient
	@JsonProperty("categoryCode")
	private String categoryCode;

	// ------------------------------------------------
	// NEW: first image for quick display
	@Transient
	@JsonProperty("imageUrl")
	private String imageUrl;

	// NEW: optional “was” price for strike-through discount
	@Transient
	@JsonProperty("originalPrice")
	private BigDecimal originalPrice;
	// ------------------------------------------------

	@Transient
	@JsonProperty("images")
	private List<String> images = new ArrayList<>();

	@Column(name = "date_created", updatable = false)
	private Instant dateCreated;
	@Column(name = "date_updated")
	private Instant dateUpdated;

	@PrePersist
	protected void onCreate() {
		dateCreated = Instant.now();
		dateUpdated = Instant.now();
	}

	@PreUpdate
	protected void onUpdate() {
		dateUpdated = Instant.now();
	}

	public Product(Long id) {
		this.id = id;
	}
}
