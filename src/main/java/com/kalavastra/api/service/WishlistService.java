package com.kalavastra.api.service;

import com.kalavastra.api.model.*;
import com.kalavastra.api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishlistService {
	private final WishlistRepository wishlistRepo;
	private final WishlistItemRepository itemRepo;
	private final ProductService productService;

	/**
	 * Fetch all wishlist items for this user and fully hydrate each product (with
	 * images, imageUrl, etc).
	 */
	@Transactional(readOnly = true)
	public List<WishlistItem> listItems(String userId) {
		return wishlistRepo.findByUserId(userId).map(wl -> {
			List<WishlistItem> items = itemRepo.findByWishlist(wl);
			// hydrate each product
			items.forEach(wi -> {
				var full = productService.getById(wi.getProduct().getId());
				wi.setProduct(full);
			});
			return items;
		}).orElse(List.of());
	}

	/**
	 * Toggle the wishlist state for this product: - If no row exists, INSERT
	 * is_active=true - If exists, flip isActive and save()
	 */
	@Transactional
	public Wishlist toggleItem(String userId, Long productId) {
		Wishlist wl = wishlistRepo.findByUserId(userId)
				.orElseGet(() -> wishlistRepo.save(Wishlist.builder().userId(userId).name("My Favourites") // default
																											// name
						.build()));

		// lookup existing row
		itemRepo.findByWishlistAndProduct_Id(wl, productId).ifPresentOrElse(wi -> {
			// flip active
			wi.setIsActive(!Boolean.TRUE.equals(wi.getIsActive()));
			itemRepo.save(wi);
		}, () -> {
			// create new
			WishlistItem newItem = WishlistItem.builder().id(new WishlistItemId(productId, wl.getWishlistId()))
					.wishlist(wl).product(new Product(productId)) // only sets id
					.isActive(true).build();
			itemRepo.save(newItem);
		});

		// now safely refresh the *contents* of the persistent collection:
		List<WishlistItem> all = itemRepo.findByWishlist(wl);
		// clear-but-not-replace:
		wl.getItems().clear();
		wl.getItems().addAll(all);
		return wl;
	}

	/**
	 * Now clears *all* items with one SQL UPDATE.
	 */
	@Transactional
	public Wishlist clearWishlist(String userId) {
		Wishlist wl = wishlistRepo.findByUserId(userId)
				.orElseThrow(() -> new IllegalArgumentException("Wishlist not found for user: " + userId));

		// bulk‐deactivate in one go
		itemRepo.deactivateAllByWishlist(wl);

		// clear the in‐memory list so Hibernate doesn’t orphan‐delete
		wl.getItems().clear();
		return wl;
	}

	/** returns true if there is an active wishlist_item for this user + product */
	@Transactional(readOnly = true)
	public boolean isWishlisted(String userId, Long productId) {
		return wishlistRepo.findByUserId(userId)
				.flatMap(wl -> itemRepo.findByWishlistAndProduct_IdAndIsActive(wl, productId, true)).isPresent();
	}
}
