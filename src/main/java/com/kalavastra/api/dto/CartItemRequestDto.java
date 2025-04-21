package com.kalavastra.api.dto;

import lombok.*;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Payload to add or update one line in a cart.
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CartItemRequestDto {

    @NotBlank
    private String productCode;

    @NotNull @Min(1)
    private Integer quantity;
}
