package com.kalavastra.api.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "wishlists")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Wishlist {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "wishlist_id")
	private Long wishlistId;

	@Builder.Default
	@Column(name = "name", length = 100, nullable = false)
	private String name = "My Favourites";

	@Column(name = "user_id", nullable = false, length = 50)
	private String userId;

	@CreationTimestamp
	@Column(name = "date_created", updatable = false)
	private OffsetDateTime dateCreated;

	@UpdateTimestamp
	@Column(name = "date_updated")
	private OffsetDateTime dateUpdated;

	@OneToMany(mappedBy = "wishlist", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<WishlistItem> items = new ArrayList<>();
}
