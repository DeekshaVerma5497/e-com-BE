package com.kalavastra.api.dto;

import lombok.*;

/**
 * DTO used for creating or returning Category information.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {

    private String categoryCode;

    private String name;

    private String description;
}
