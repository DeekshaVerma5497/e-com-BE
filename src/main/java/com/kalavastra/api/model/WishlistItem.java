package com.kalavastra.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@Entity
@Table(name = "wishlist_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WishlistItem {

	@EmbeddedId
	private WishlistItemId id;

	// hide the back-ref so JSON only shows product, dateCreated, isActive
	@MapsId("wishlistId")
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "wishlist_id", nullable = false)
	@JsonIgnore
	private Wishlist wishlist;

	@MapsId("productId")
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;

	@CreationTimestamp
	@Column(name = "date_created", updatable = false)
	private OffsetDateTime dateCreated;

	@Column(name = "is_active", nullable = false)
	private Boolean isActive = true;

	/** expose the PK fields in top-level JSON too */
	@Transient
	@JsonProperty("productId")
	public Long getProductId() {
		return this.id != null ? this.id.getProductId() : null;
	}

	@Transient
	@JsonProperty("wishlistId")
	public Long getWishlistId() {
		return this.id != null ? this.id.getWishlistId() : null;
	}
}
