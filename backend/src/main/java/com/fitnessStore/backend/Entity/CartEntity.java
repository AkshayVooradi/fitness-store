package com.fitnessStore.backend.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fitnessStore.backend.StorageClasses.CartItemClass;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "cart_entity")
@EqualsAndHashCode(exclude = "user")
public class CartEntity {

    @Id
    private ObjectId id;

    @DBRef
    @Field("user_id")
    @JsonIgnore
    private UserEntity user;

    private List<CartItemClass> products;
}
