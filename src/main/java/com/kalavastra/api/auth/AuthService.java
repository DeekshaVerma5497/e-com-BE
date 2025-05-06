package com.kalavastra.api.auth;

import com.kalavastra.api.exception.ResourceNotFoundException;
import com.kalavastra.api.model.User;
import com.kalavastra.api.repository.UserRepository;
import com.kalavastra.api.service.UserService;
import com.kalavastra.api.security.JwtUtil;
import lombok.RequiredArgsConstructor;

import java.security.SecureRandom;
import java.util.Optional;

import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepo;
    private static final String ALPHANUM = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom RNG = new SecureRandom();

    public AuthResponse signup(User user) {
    	Optional<User> userExists = userRepo.findByEmail(user.getEmail());
        if (userExists == null) {
            throw new IllegalStateException("Email already in use");
        }

        user.setUserId(makeUserId(user.getName()));
        // encode the transient password into the real hash
        String raw = user.getPassword();
        user.setPasswordHash(passwordEncoder.encode(raw));

        // clear the transient raw password so it never gets reused
        user.setPassword(null);
        // generate a userId, set defaults, etc.
        userRepo.save(user);

        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(token);
    }
    
    private String makeUserId(String name) {
        // squash the name to lowercase letters and digits only
        String base = (name == null
            ? "user"
            : name.trim().toLowerCase().replaceAll("[^a-z0-9]+", "")
        );
        return base + "_" + randomSuffix(5);
    }

    private String randomSuffix(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(ALPHANUM.charAt(RNG.nextInt(ALPHANUM.length())));
        }
        return sb.toString();
    }

    public AuthResponse login(AuthRequest req) {
        Authentication auth = authManager.authenticate(
            new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
        );
        var ud = (org.springframework.security.core.userdetails.User) auth.getPrincipal();
        String token = jwtUtil.generateToken(ud.getUsername());
        return AuthResponse.builder().token(token).build();
    }
    
    public User getCurrentUser() {
        // Spring Security stores the username (email) in the Authentication's principal
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }
}
