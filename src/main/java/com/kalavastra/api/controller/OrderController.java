package com.kalavastra.api.controller;

import com.kalavastra.api.auth.AuthService;
import com.kalavastra.api.model.Order;
import com.kalavastra.api.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;
	private final AuthService authService;

	@Operation(summary = "List all orders for the current user")
	@GetMapping
	public ResponseEntity<List<Order>> listOrders() {
		String uid = authService.getCurrentUser().getUserId();
		return ResponseEntity.ok(orderService.listOrders(uid));
	}

	@Operation(summary = "Get a single order by ID")
	@GetMapping("/{orderId}")
	public ResponseEntity<Order> getOrder(@PathVariable Long orderId) {
		String uid = authService.getCurrentUser().getUserId();
		return ResponseEntity.ok(orderService.getOrder(uid, orderId));
	}

	@Operation(summary = "Place a new order for the current user")
	@PostMapping
	public ResponseEntity<Order> placeOrder(@RequestBody Order order) {
		String uid = authService.getCurrentUser().getUserId();
		return ResponseEntity.ok(orderService.placeOrder(uid, order));
	}

	@Operation(summary = "Cancel an order for the current user")
	@DeleteMapping("/{orderId}")
	public ResponseEntity<Order> cancelOrder(@PathVariable Long orderId) {
		String uid = authService.getCurrentUser().getUserId();
		return ResponseEntity.ok(orderService.cancelOrder(uid, orderId));
	}
}
