package com.fitnessStore.backend.StorageClasses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponse {
    private List<CartItemClass> items;
}
