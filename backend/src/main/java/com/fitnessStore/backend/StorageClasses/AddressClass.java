package com.fitnessStore.backend.StorageClasses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressEntity {
    private String streetAddress;

    private String city;

    private String state;

    private String zipcode;

}
