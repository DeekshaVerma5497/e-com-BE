package com.kalavastra.api.service;

import com.kalavastra.api.auth.AuthService;
import com.kalavastra.api.exception.ResourceNotFoundException;
import com.kalavastra.api.model.Address;
import com.kalavastra.api.model.User;
import com.kalavastra.api.repository.AddressRepository;
import com.kalavastra.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepo;
    private final UserRepository userRepo;
    private final AuthService authService;

    @Transactional
    public Address create(Address req) {
        User user = authService.getCurrentUser();
        req.setUser(user);
        return addressRepo.save(req);
    }

    @Transactional(readOnly = true)
    public List<Address> list() {
        User user = authService.getCurrentUser();
        return addressRepo.findAllByUser(user);
    }

    @Transactional(readOnly = true)
    public Address get(Long addressId) {
        User user = authService.getCurrentUser();
        return addressRepo.findByAddressIdAndUser(addressId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId.toString()));
    }

    @Transactional
    public Address update(Long addressId, Address req) {
        Address existing = get(addressId);
        // copy updatable fields
        existing.setName(req.getName());
        existing.setPhoneNumber(req.getPhoneNumber());
        existing.setAddressLine1(req.getAddressLine1());
        existing.setAddressLine2(req.getAddressLine2());
        existing.setCity(req.getCity());
        existing.setState(req.getState());
        existing.setPincode(req.getPincode());
        existing.setIsDefault(req.getIsDefault());
        existing.setIsActive(req.getIsActive());
        return addressRepo.save(existing);
    }

    @Transactional
    public void delete(Long addressId) {
        Address existing = get(addressId);
        addressRepo.delete(existing);
    }
}
