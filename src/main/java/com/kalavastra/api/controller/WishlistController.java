package com.kalavastra.api.controller;

import com.kalavastra.api.auth.AuthService;
import com.kalavastra.api.model.Wishlist;
import com.kalavastra.api.model.WishlistItem;
import com.kalavastra.api.service.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/wishlist")
@RequiredArgsConstructor
public class WishlistController {
	private final WishlistService wishlistService;
	private final AuthService authService;

	@GetMapping("/items")
	public ResponseEntity<List<WishlistItem>> listItems() {
		String uid = authService.getCurrentUser().getUserId();
		return ResponseEntity.ok(wishlistService.listItems(uid));
	}

	/**
	 * POST /items Toggle a product in the wishlist: - if not present or inactive →
	 * add/activate - if active → deactivate
	 */
	@PostMapping("/items")
	@Operation(summary = "Toggle a product in the wishlist")
	public ResponseEntity<Wishlist> toggleItem(@RequestBody Map<String, Long> body) {
		Long productId = Objects.requireNonNull(body.get("productId"), "productId is required");
		String uid = authService.getCurrentUser().getUserId();
		return ResponseEntity.ok(wishlistService.toggleItem(uid, productId));
	}

	/**
	 * DELETE /items Clear the entire wishlist
	 */
	@DeleteMapping("/items")
	@Operation(summary = "Clear the wishlist")
	public ResponseEntity<Wishlist> clear() {
		String uid = authService.getCurrentUser().getUserId();
		return ResponseEntity.ok(wishlistService.clearWishlist(uid));
	}
}
