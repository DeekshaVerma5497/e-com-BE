package com.kalavastra.api.controller;

import com.kalavastra.api.model.User;
import com.kalavastra.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService svc;

    @PostMapping
    public ResponseEntity<User> signup(@RequestBody User u) {
        return ResponseEntity.ok(svc.create(u));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> get(@PathVariable String userId) {
        return ResponseEntity.ok(svc.getByUserId(userId));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> update(
        @PathVariable String userId,
        @RequestBody User u
    ) {
        return ResponseEntity.ok(svc.update(userId, u));
    }
}
