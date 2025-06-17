package com.kalavastra.api.controller;

import com.kalavastra.api.auth.AuthService;
import com.kalavastra.api.model.CartItem;
import com.kalavastra.api.model.User;
import com.kalavastra.api.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

	private final CartService cartService;
	private final AuthService authService;

	@Operation(summary = "List all items in the current user's cart")
	@GetMapping("/items")
	public ResponseEntity<List<CartItem>> listItems() {
		User me = authService.getCurrentUser();
		return ResponseEntity.ok(cartService.listItems(me.getUserId()));
	}

	@Operation(summary = "Add (or increment) an item in the current user's cart")
	@PostMapping("/items")
	public ResponseEntity<List<CartItem>> addItem(@RequestBody CartItem item) {
		User me = authService.getCurrentUser();
		return ResponseEntity.ok(cartService.addItem(me.getUserId(), item));
	}

	@Operation(summary = "Remove an item from the current user's cart")
	@DeleteMapping("/items/{itemId}")
	public ResponseEntity<List<CartItem>> removeItem(@PathVariable Long itemId) {
		User me = authService.getCurrentUser();
		return ResponseEntity.ok(cartService.removeItem(me.getUserId(), itemId));
	}

	@Operation(summary = "Clear the current user's cart")
	@DeleteMapping("/items")
	public ResponseEntity<List<CartItem>> clearCart() {
		User me = authService.getCurrentUser();
		return ResponseEntity.ok(cartService.clearCart(me.getUserId()));
	}
}
