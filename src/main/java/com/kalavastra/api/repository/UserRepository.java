package com.kalavastra.api.repository;

import com.kalavastra.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUserId(String userId);

	Optional<User> findByEmail(String email);
}
