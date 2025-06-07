package com.kalavastra.api.controller;

import com.kalavastra.api.model.Cart;
import com.kalavastra.api.model.CartItem;
import com.kalavastra.api.service.CartService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users/{userId}/cart")
@RequiredArgsConstructor
public class CartController {

	private final CartService svc;

	@Operation(summary = "Create a new cart for a user")
	@PostMapping
	public ResponseEntity<Cart> create(@PathVariable String userId) {
		Cart created = svc.createCart(userId);
		return ResponseEntity.status(201).body(created);
	}

	@Operation(summary = "Get cart details for a user")
	@GetMapping
	public ResponseEntity<Cart> get(@PathVariable String userId) {
		return ResponseEntity.ok(svc.getCart(userId));
	}

	@Operation(summary = "Delete cart for a user")
	@DeleteMapping
	public ResponseEntity<Void> deleteCart(@PathVariable String userId) {
		svc.deleteCart(userId);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "Adjust quantity of an existing cart item (pos=inc, neg=dec; removes if <1)")
	@PatchMapping("/items/{itemId}/quantity")
	public ResponseEntity<Cart> adjustQuantity(@PathVariable String userId, @PathVariable Long itemId,
			@RequestParam int delta) {
		return ResponseEntity.ok(svc.adjustItemQuantity(userId, itemId, delta));
	}

	@Operation(summary = "Add an item to the cart")
	@PostMapping("/items")
	public ResponseEntity<Cart> addItem(@PathVariable String userId, @RequestParam String productCode,
			@RequestParam int quantity) {
		return ResponseEntity.ok(svc.addItem(userId, productCode, quantity));
	}

	@Operation(summary = "Get all items of a cart")
	@GetMapping("/items")
	public ResponseEntity<List<CartItem>> listItems(@PathVariable String userId) {
		return ResponseEntity.ok(svc.listItems(userId));
	}

	@Operation(summary = "Remove an item from the cart")
	@DeleteMapping("/items/{itemId}")
	public ResponseEntity<Cart> removeItem(@PathVariable String userId, @PathVariable Long itemId) {
		return ResponseEntity.ok(svc.removeItem(userId, itemId));
	}
}
