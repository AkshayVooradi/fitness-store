package com.fitnessStore.backend.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "product_entity")
public class ProductEntity {

    @Id
    private ObjectId id;

    @Indexed(unique = true)
    private String title;

    private String category;

    private String brand;

    private double price;

    private int discountPercent;

    private String description;

    private String imageUrl;

    private int stock;

    private double sumOfRatings;

    @DBRef
    @JsonIgnore
    private List<ReviewEntity> reviews;

    private List<String> size= new ArrayList<>();
}
