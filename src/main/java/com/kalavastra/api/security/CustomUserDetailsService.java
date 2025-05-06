package com.kalavastra.api.security;


import org.springframework.security.core.userdetails.User;    // <<-- security.User builder
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kalavastra.api.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
  private final UserRepository repo;

  @Override
  public UserDetails loadUserByUsername(String email) {
    // Fully‐qualified name for your JPA User entity:
    com.kalavastra.api.model.User u = repo.findByEmail(email)
      .orElseThrow(() -> new UsernameNotFoundException("No user for email: " + email));

    // Now this 'User' is definitely Spring Security's builder:
    return User
      .withUsername(u.getEmail())
      .password(u.getPasswordHash())
      .authorities("USER")
      .accountExpired(false)
      .accountLocked(false)
      .credentialsExpired(false)
      .disabled(!u.getIsActive())
      .build();
  }
}