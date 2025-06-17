package com.kalavastra.api.controller;

import com.kalavastra.api.auth.AuthService;
import com.kalavastra.api.model.User;
import com.kalavastra.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(exposedHeaders = "X-Require-Logout")
@RequiredArgsConstructor
public class UserController {
	private final UserService svc;
	private final AuthService auth;

	@PostMapping
	public ResponseEntity<User> signup(@RequestBody User u) {
		return ResponseEntity.ok(svc.create(u));
	}

	@GetMapping("/{userId}")
	public ResponseEntity<User> get(@PathVariable String userId) {
		return ResponseEntity.ok(svc.getByUserId(userId));
	}

	@PutMapping("/{userId}")
	public ResponseEntity<User> update(@PathVariable String userId, @RequestBody User u) {
		return ResponseEntity.ok(svc.update(userId, u));
	}

	/** Get the current user’s profile */
	@GetMapping("/me")
	public ResponseEntity<User> getProfile() {
		String uid = auth.getCurrentUser().getUserId();
		User u = svc.getByUserId(uid);
		return ResponseEntity.ok(u);
	}

	/** Update the current user’s profile */
	@PutMapping("/me")
	public ResponseEntity<User> updateMyProfile(@RequestBody User payload) {
		// 1) who am I?
		User me = auth.getCurrentUser();
		String oldEmail = me.getEmail();

		// 2) do the update
		User updated = svc.update(me.getUserId(), payload);

		// 3) if the email actually changed, force a logout
		HttpHeaders headers = new HttpHeaders();
		if (!oldEmail.equalsIgnoreCase(updated.getEmail())) {
			SecurityContextHolder.clearContext();
			headers.add("X-Require-Logout", "true");
		}

		return ResponseEntity.ok().headers(headers).body(updated);
	}
}
