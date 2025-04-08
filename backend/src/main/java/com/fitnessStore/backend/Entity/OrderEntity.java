package com.fitnessStore.backend.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fitnessStore.backend.StorageClasses.AddressClass;
import com.fitnessStore.backend.StorageClasses.CartItemClass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "order_entity")
public class OrderEntity {
    @Id
    private String id;

    @DBRef
    @Field("user_id")
    @JsonIgnore
    private UserEntity user;

    private String userId;

    private List<CartItemClass> cartItems;

    private String orderStatus;

    private LocalDateTime orderDate;

    private LocalDateTime orderUpdateDate;

    private double totalAmount;

    private boolean isCancelled;
}
