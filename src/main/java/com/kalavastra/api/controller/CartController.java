package com.kalavastra.api.controller;

import com.kalavastra.api.model.Cart;
import com.kalavastra.api.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/v1/users/{userId}/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService svc;

    @PostMapping
    public ResponseEntity<Cart> getOrCreate(@PathVariable String userId) {
        return ResponseEntity.ok(svc.getOrCreate(userId));
    }

    @GetMapping
    public ResponseEntity<Cart> get(@PathVariable String userId) {
        return ResponseEntity.ok(svc.getCart(userId));
    }

    @PostMapping("/items")
    public ResponseEntity<Cart> addItem(
        @PathVariable String userId,
        @RequestParam String productCode,
        @RequestParam int quantity
    ) {
        return ResponseEntity.ok(svc.addItem(userId, productCode, quantity));
    }

    @PutMapping("/items/{itemId}")
    public ResponseEntity<Cart> updateItem(
        @PathVariable String userId,
        @PathVariable Long itemId,
        @RequestParam int quantity
    ) {
        return ResponseEntity.ok(svc.updateItem(userId, itemId, quantity));
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Cart> removeItem(
        @PathVariable String userId,
        @PathVariable Long itemId
    ) {
        return ResponseEntity.ok(svc.removeItem(userId, itemId));
    }

    @DeleteMapping("/items")
    public ResponseEntity<Void> clear(
        @PathVariable String userId
    ) {
        svc.clearCart(userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteCart(@PathVariable String userId) {
        svc.deleteCart(userId);
        return ResponseEntity.noContent().build();
    }
}
