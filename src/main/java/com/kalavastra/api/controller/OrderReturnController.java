package com.kalavastra.api.controller;

import com.kalavastra.api.auth.AuthService;
import com.kalavastra.api.model.OrderReturn;
import com.kalavastra.api.service.OrderReturnService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/returns")
@RequiredArgsConstructor
public class OrderReturnController {

	private final OrderReturnService returnService;
	private final AuthService authService;

	@Operation(summary = "List all return requests for the current user")
	@GetMapping
	public ResponseEntity<List<OrderReturn>> listReturns() {
		String uid = authService.getCurrentUser().getUserId();
		return ResponseEntity.ok(returnService.listReturns(uid));
	}

	@Operation(summary = "Get a specific return request")
	@GetMapping("/{returnId}")
	public ResponseEntity<OrderReturn> getReturn(@PathVariable Long returnId) {
		String uid = authService.getCurrentUser().getUserId();
		return ResponseEntity.ok(returnService.getReturn(uid, returnId));
	}

	@Operation(summary = "Request a return for a specific order item")
	@PostMapping("/{orderItemId}")
	public ResponseEntity<OrderReturn> requestReturn(@PathVariable Long orderItemId,
			@RequestBody OrderReturn returnPayload) {
		String uid = authService.getCurrentUser().getUserId();
		return ResponseEntity.ok(returnService.requestReturn(uid, orderItemId, returnPayload));
	}

	@Operation(summary = "Update an existing return request")
	@PutMapping("/{returnId}")
	public ResponseEntity<OrderReturn> updateReturn(@PathVariable Long returnId, @RequestBody OrderReturn updates) {
		String uid = authService.getCurrentUser().getUserId();
		return ResponseEntity.ok(returnService.updateReturn(uid, returnId, updates));
	}

	@Operation(summary = "Cancel a return request")
	@DeleteMapping("/{returnId}")
	public ResponseEntity<OrderReturn> cancelReturn(@PathVariable Long returnId) {
		String uid = authService.getCurrentUser().getUserId();
		return ResponseEntity.ok(returnService.cancelReturn(uid, returnId));
	}
}
