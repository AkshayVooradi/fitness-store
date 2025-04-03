package com.fitnessStore.backend.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fitnessStore.backend.StorageClasses.AddressClass;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "user_entity")
public class UserEntity {

    @Id
    private ObjectId id;

    @Indexed(unique = true)
    private String username;

    private String password;

    @Indexed(unique = true)
    private String email;


    private String mobile;

    private String role;

    private LocalDateTime createdAt;

    @DBRef
    @Field("cart_id")
    @JsonIgnore
    private CartEntity cart;


    private Set<AddressClass> address=new HashSet<>();

    @DBRef
    private List<OrderEntity> orders = new ArrayList<>();

    @DBRef
    private List<ReviewEntity> reviews = new ArrayList<>();

    @DBRef
    private List<ProductEntity> products = new ArrayList<>();
}
