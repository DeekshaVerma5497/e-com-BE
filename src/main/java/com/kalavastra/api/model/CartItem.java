package com.kalavastra.api.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.OffsetDateTime;

@Entity
@Table(name = "cart_items", schema = "kalavastra")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cart_item_id")
	private Long cartItemId;

	@CreationTimestamp
	@Column(name = "date_created", updatable = false)
	private OffsetDateTime dateCreated;

	@UpdateTimestamp
	@Column(name = "date_updated")
	private OffsetDateTime dateUpdated;

	@Column(name = "is_active", nullable = false)
	private Boolean isActive;

	@Column(name = "quantity", nullable = false)
	private Integer quantity;

	// --- ignore back-pointer to Cart to avoid infinite recursion ---
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "cart_id", nullable = false)
	@JsonIgnore
	private Cart cart;

	// --- FULL product details now included in JSON ---
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;

	// expose productId as top-level field
	@Transient
	@JsonProperty("productId")
	private Long productId;

	@JsonSetter("productId")
	public void setProductId(Long productId) {
		this.productId = productId;
		this.product = new Product(productId);
	}

	@JsonGetter("productId")
	public Long getProductId() {
		return (product != null ? product.getId() : productId);
	}
}
