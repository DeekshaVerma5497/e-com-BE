// src/main/java/com/kalavastra/api/repository/AddressRepository.java
package com.kalavastra.api.repository;

import com.kalavastra.api.model.Address;
import com.kalavastra.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findAllByUser(User user);
}
