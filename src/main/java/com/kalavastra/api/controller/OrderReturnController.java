package com.kalavastra.api.controller;

import com.kalavastra.api.model.OrderReturn;
import com.kalavastra.api.service.OrderReturnService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/{userId}/returns")
public class OrderReturnController {

	private final OrderReturnService svc;

	@Operation(summary = "Request a return for an order item")
	@PostMapping
	public ResponseEntity<OrderReturn> requestReturn(@PathVariable String userId, @RequestParam Long orderItemId,
			@RequestParam String reason, @RequestParam String refundMethod) {
		return ResponseEntity.ok(svc.requestReturn(userId, orderItemId, reason, refundMethod));
	}

	@Operation(summary = "Get a single return by its ID")
	@GetMapping("/{returnId}")
	public ResponseEntity<OrderReturn> getReturn(@PathVariable Long returnId) {
		return ResponseEntity.ok(svc.getReturn(returnId));
	}

	@Operation(summary = "List all returns for this user")
	@GetMapping
	public ResponseEntity<List<OrderReturn>> listForUser(@PathVariable String userId) {
		return ResponseEntity.ok(svc.listForUser(userId));
	}

	@Operation(summary = "List all returns for a specific order")
	@GetMapping("/order/{orderId}")
	public ResponseEntity<List<OrderReturn>> listForOrder(@PathVariable String userId, @PathVariable Long orderId) {
		return ResponseEntity.ok(svc.listForOrder(orderId));
	}

	@Operation(summary = "Update a return (e.g. change status or refundMethod)")
	@PatchMapping("/{returnId}")
	public ResponseEntity<OrderReturn> updateReturn(@PathVariable String userId, @PathVariable Long returnId,
			@RequestParam(required = false) String status, @RequestParam(required = false) String refundMethod) {
		OrderReturn updated = svc.updateReturn(userId, returnId, status, refundMethod);
		return ResponseEntity.ok(updated);
	}
}
