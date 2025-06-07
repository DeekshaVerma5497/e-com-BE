package com.kalavastra.api.controller;

import com.kalavastra.api.model.Order;
import com.kalavastra.api.model.OrderItem;
import com.kalavastra.api.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
	private final OrderService svc;

	@PostMapping("/{userId}")
	public ResponseEntity<Order> place(@PathVariable String userId, @RequestBody List<OrderItem> items,
			@RequestParam Long addressId) {
		return ResponseEntity.ok(svc.place(userId, addressId, items));
	}

	@GetMapping("/{orderId}")
	public ResponseEntity<Order> get(@PathVariable Long orderId) {
		return ResponseEntity.ok(svc.getById(orderId));
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<List<Order>> listForUser(@PathVariable String userId) {
		return ResponseEntity.ok(svc.listForUser(userId));
	}

	@PutMapping("/{orderId}/cancel")
	public ResponseEntity<Order> cancel(@PathVariable Long orderId) {
		return ResponseEntity.ok(svc.cancel(orderId));
	}
}
