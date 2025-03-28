package com.fitnessStore.backend.StorageClasses;

import com.fitnessStore.backend.Entity.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Component
public class CartItemClass {

    @DBRef
    private ProductEntity product;

    private int quantity;

    private String size;

    private double cost;

    private String status;

    private LocalDateTime deliveredAt;
}
