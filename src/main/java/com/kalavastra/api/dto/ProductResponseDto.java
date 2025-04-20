package com.kalavastra.api.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * DTO for sending product details back to client.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponseDto {

    private String productCode;

    private String name;

    private String description;

    private BigDecimal price;

    private Integer stock;

    private String imageUrl;

    private String categoryName;

    private Instant dateCreated;

    private Instant dateUpdated;
}
