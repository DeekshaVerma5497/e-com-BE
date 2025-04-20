package com.kalavastra.api.repository;

import com.kalavastra.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * CRUD + finder for {@link User}.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Lookup by email (for login).
     */
    Optional<User> findByEmail(String email);

    /**
     * Lookup by business userId.
     */
    Optional<User> findByUserId(String userId);
}
