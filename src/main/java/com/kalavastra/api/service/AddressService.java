// src/main/java/com/kalavastra/api/service/AddressService.java
package com.kalavastra.api.service;

import com.kalavastra.api.dto.*;
import com.kalavastra.api.exception.ResourceNotFoundException;
import com.kalavastra.api.mapper.DomainMapper;
import com.kalavastra.api.model.*;
import com.kalavastra.api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepo;
    private final UserRepository userRepo;
    private final DomainMapper mapper;

    @Transactional
    public AddressResponseDto create(AddressRequestDto dto) {
        User user = userRepo.findByUserId(dto.getUserId())
            .orElseThrow(() -> new ResourceNotFoundException("User", "userId", dto.getUserId()));

        var address = mapper.dtoToAddress(dto);
        address.setUser(user);
        address.setIsActive(true);
        // handle defaults if needed...

        return mapper.addressToDto(addressRepo.save(address));
    }

    @Transactional(readOnly = true)
    public List<AddressResponseDto> listByUser(String userId) {
        User user = userRepo.findByUserId(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));

        return addressRepo.findAllByUser(user).stream()
            .map(mapper::addressToDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AddressResponseDto get(Long id) {
        return addressRepo.findById(id)
            .map(mapper::addressToDto)
            .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", id.toString()));
    }

    @Transactional
    public AddressResponseDto update(Long id, AddressRequestDto dto) {
        var address = addressRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", id.toString()));

        mapper.updateAddressFromDto(dto, address);
        // if userId changed (unlikely) you could re-fetch user here...

        return mapper.addressToDto(addressRepo.save(address));
    }

    @Transactional
    public void delete(Long id) {
        if (!addressRepo.existsById(id)) {
            throw new ResourceNotFoundException("Address", "addressId", id.toString());
        }
        addressRepo.deleteById(id);
    }
}
