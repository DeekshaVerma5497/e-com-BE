package com.kalavastra.api.repository;

import com.kalavastra.api.model.Wishlist;
import com.kalavastra.api.model.WishlistItem;
import com.kalavastra.api.model.WishlistItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistItemRepository extends JpaRepository<WishlistItem, WishlistItemId> {
	List<WishlistItem> findByWishlistAndIsActive(Wishlist wl, Boolean isActive);

	Optional<WishlistItem> findByWishlistAndProduct_IdAndIsActive(Wishlist wl, Long productId, Boolean isActive);

	List<WishlistItem> findByWishlist(Wishlist wishlist);

	Optional<WishlistItem> findByWishlistAndProduct_Id(Wishlist wishlist, Long productId);

	// Bulk‐deactivate all active items in one go:
	@Modifying
	@Query("""
			    UPDATE WishlistItem wi
			       SET wi.isActive = false
			     WHERE wi.wishlist = :wishlist
			       AND wi.isActive = true
			""")
	void deactivateAllByWishlist(@Param("wishlist") Wishlist wl);
}
