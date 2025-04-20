// src/main/java/com/kalavastra/api/controller/AddressController.java
package com.kalavastra.api.controller;

import com.kalavastra.api.dto.*;
import com.kalavastra.api.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @Operation(summary = "Create a new address for a user")
    @PostMapping
    public ResponseEntity<AddressResponseDto> create(
        @Valid @RequestBody AddressRequestDto dto
    ) {
        return ResponseEntity.ok(addressService.create(dto));
    }

    @Operation(summary = "List all addresses for a given user")
    @GetMapping
    public ResponseEntity<List<AddressResponseDto>> list(
        @RequestParam String userId
    ) {
        return ResponseEntity.ok(addressService.listByUser(userId));
    }

    @Operation(summary = "Get a single address by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<AddressResponseDto> get(@PathVariable Long id) {
        return ResponseEntity.ok(addressService.get(id));
    }

    @Operation(summary = "Update an existing address")
    @PutMapping("/{id}")
    public ResponseEntity<AddressResponseDto> update(
        @PathVariable Long id,
        @Valid @RequestBody AddressRequestDto dto
    ) {
        return ResponseEntity.ok(addressService.update(id, dto));
    }

    @Operation(summary = "Delete an address")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        addressService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
