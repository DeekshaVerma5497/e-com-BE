// src/main/java/com/kalavastra/api/security/CustomUserDetailsService.java
package com.kalavastra.api.security;

import com.kalavastra.api.model.User;
import com.kalavastra.api.repository.UserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepo;

    public CustomUserDetailsService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        User user = userRepo.findByEmail(email)
            .orElseThrow(() -> 
                new UsernameNotFoundException("No user with email " + email)
            );

        return org.springframework.security.core.userdetails.User.builder()
            .username(user.getEmail())
            .password(user.getPasswordHash())
            .authorities("ROLE_USER")
            .accountExpired(false)
            .accountLocked(false)
            .credentialsExpired(false)
            .disabled(!user.getIsActive())
            .build();
    }
}
