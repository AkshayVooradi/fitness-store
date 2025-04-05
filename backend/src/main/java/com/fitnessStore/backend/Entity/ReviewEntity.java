package com.fitnessStore.backend.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "review_entity")
public class ReviewEntity {
    @Id
    private ObjectId id;

    @DBRef
    @Field("user_id")
    @JsonIgnore
    private UserEntity user;

    @DBRef
    @Field("product_id")
    @JsonIgnore
    private ProductEntity product;

    private String userId;

    private String userName;

    private String productId;

    private String reviewMessage;

    private double reviewValue;

}