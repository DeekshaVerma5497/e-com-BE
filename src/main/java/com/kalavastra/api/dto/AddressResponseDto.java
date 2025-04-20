package com.kalavastra.api.dto;

import lombok.*;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressResponseDto {
    private Long addressId;
    private String userId;
    private String name;
    private String phoneNumber;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String pincode;
    private Boolean isDefault;
    private Boolean isActive;
    private Instant dateCreated;
    private Instant dateUpdated;
}
