package com.kalavastra.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WishlistItemId implements Serializable {
	@Column(name = "product_id")
	private Long productId;

	@Column(name = "wishlist_id")
	private Long wishlistId;
}
