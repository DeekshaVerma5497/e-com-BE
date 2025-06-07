package com.kalavastra.api.repository;

import com.kalavastra.api.model.WishlistItem;
import com.kalavastra.api.model.WishlistItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface WishlistItemRepository extends JpaRepository<WishlistItem, WishlistItemId> {

	List<WishlistItem> findAllByWishlist_WishlistIdAndIsActiveTrue(Long wishlistId);

	Optional<WishlistItem> findByWishlist_WishlistIdAndProduct_ProductCode(Long wishlistId, String productCode);
}
