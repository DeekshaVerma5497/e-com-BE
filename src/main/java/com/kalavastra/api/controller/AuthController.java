package com.kalavastra.api.controller;

import com.kalavastra.api.auth.*;
import com.kalavastra.api.model.User;

import lombok.RequiredArgsConstructor;

import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
	private final AuthService svc;

	@PostMapping("/signup")
	public ResponseEntity<AuthResponse> signup(@RequestBody User user) throws BadRequestException {
		if (user.getPassword() == null || user.getPassword().isBlank()) {
			throw new BadRequestException("Password is required");
		}
		return ResponseEntity.ok(svc.signup(user));
	}

	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest req) {
		return ResponseEntity.ok(svc.login(req));
	}
}
