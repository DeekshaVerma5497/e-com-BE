package com.kalavastra.api.dto;

import lombok.*;

import java.math.BigDecimal;

/**
 * DTO for incoming product create/update requests.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequestDto {

    private String productCode;

    private String name;

    private String description;

    private BigDecimal price;

    private Integer stock;

    private String imageUrl;

    private String categoryCode;
}
