package com.kalavastra.api.service;

import com.kalavastra.api.exception.ResourceNotFoundException;
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
	private final UserService userService;
	private final ProductService productService;

	@Transactional
	public Wishlist createWishlist(String userId, String name) {
		var user = userService.getByUserId(userId);
		Wishlist w = Wishlist.builder().user(user).name(name).build();
		return wishlistRepo.save(w);
	}

	@Transactional(readOnly = true)
	public List<Wishlist> listWishlists(String userId) {
		userService.getByUserId(userId);
		return wishlistRepo.findByUser_UserId(userId);
	}

	@Transactional
	public Wishlist renameWishlist(String userId, Long wishlistId, String newName) {
		Wishlist w = wishlistRepo.findById(wishlistId).filter(ws -> ws.getUser().getUserId().equals(userId))
				.orElseThrow(() -> new ResourceNotFoundException("Wishlist", "id", wishlistId.toString()));
		w.setName(newName);
		return wishlistRepo.save(w);
	}

	@Transactional
	public void deleteWishlist(String userId, Long wishlistId) {
		Wishlist w = wishlistRepo.findById(wishlistId).filter(ws -> ws.getUser().getUserId().equals(userId))
				.orElseThrow(() -> new ResourceNotFoundException("Wishlist", "id", wishlistId.toString()));
		wishlistRepo.delete(w);
	}

	@Transactional(readOnly = true)
	public List<WishlistItem> listItems(String userId, Long wishlistId) {
		// ensures wishlist belongs to user
		wishlistRepo.findById(wishlistId).filter(w -> w.getUser().getUserId().equals(userId))
				.orElseThrow(() -> new ResourceNotFoundException("Wishlist", "id", wishlistId.toString()));
		return itemRepo.findAllByWishlist_WishlistIdAndIsActiveTrue(wishlistId);
	}

	@Transactional
	public Wishlist addItem(String userId, Long wishlistId, String productCode) {
		Wishlist w = wishlistRepo.findById(wishlistId).filter(ws -> ws.getUser().getUserId().equals(userId))
				.orElseThrow(() -> new ResourceNotFoundException("Wishlist", "id", wishlistId.toString()));

		var product = productService.getByCode(productCode);
		var existing = itemRepo.findByWishlist_WishlistIdAndProduct_ProductCode(wishlistId, productCode).orElse(null);

		if (existing != null) {
			existing.setIsActive(true);
			itemRepo.save(existing);
		} else {
			WishlistItem item = WishlistItem.builder().wishlist(w).product(product).build();
			w.getItems().add(item);
		}
		return wishlistRepo.save(w);
	}

	@Transactional
	public Wishlist removeItem(String userId, Long wishlistId, String productCode) {
		WishlistItem item = itemRepo.findByWishlist_WishlistIdAndProduct_ProductCode(wishlistId, productCode)
				.filter(i -> i.getWishlist().getUser().getUserId().equals(userId))
				.orElseThrow(() -> new ResourceNotFoundException("WishlistItem", "productCode", productCode));

		item.setIsActive(false);
		itemRepo.save(item);
		return item.getWishlist();
	}
}
