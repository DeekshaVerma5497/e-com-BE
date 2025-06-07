package com.kalavastra.api.repository;

import com.kalavastra.api.model.Address;
import com.kalavastra.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
	List<Address> findAllByUser(User user);

	Optional<Address> findByAddressIdAndUser(Long addressId, User user);
}
