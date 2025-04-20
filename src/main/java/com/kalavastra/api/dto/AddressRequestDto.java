package com.kalavastra.api.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressRequestDto {
    @NotBlank
    private String userId;

    @NotBlank
    private String name;

    @NotBlank
    @Size(max = 15)
    private String phoneNumber;

    @NotBlank
    private String addressLine1;

    private String addressLine2;

    @NotBlank
    private String city;

    @NotBlank
    private String state;

    @NotBlank
    @Size(min = 5, max = 10)
    private String pincode;

    private Boolean isDefault;
}
