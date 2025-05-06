package com.kalavastra.api.controller;

import com.kalavastra.api.model.Address;
import com.kalavastra.api.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/addresses")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    @Operation(summary = "Create a new address for the current user")
    @PostMapping
    public ResponseEntity<Address> create(@Valid @RequestBody Address req) {
        Address saved = addressService.create(req);
        URI location = URI.create("/api/v1/addresses/" + saved.getAddressId());
        return ResponseEntity.created(location).body(saved);
    }

    @Operation(summary = "List all addresses for the current user")
    @GetMapping
    public ResponseEntity<List<Address>> list() {
        return ResponseEntity.ok(addressService.list());
    }

    @Operation(summary = "Get a specific address by ID for the current user")
    @GetMapping("/{addressId}")
    public ResponseEntity<Address> get(@PathVariable Long addressId) {
        return ResponseEntity.ok(addressService.get(addressId));
    }

    @Operation(summary = "Update an address for the current user")
    @PutMapping("/{addressId}")
    public ResponseEntity<Address> update(
        @PathVariable Long addressId,
        @Valid @RequestBody Address req
    ) {
        return ResponseEntity.ok(addressService.update(addressId, req));
    }

    @Operation(summary = "Delete an address for the current user")
    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> delete(@PathVariable Long addressId) {
        addressService.delete(addressId);
        return ResponseEntity.noContent().build();
    }
}
