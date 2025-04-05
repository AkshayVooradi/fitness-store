package com.fitnessStore.backend.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "product_entity")
public class ProductEntity {

    @Getter
    @Id

    private ObjectId id;

    @Indexed(unique = true)
    private String title;

    private String category;

    private String brand;

    private double price;


    private int salePrice;


    private String description;

    private List<String> imageUrl;

    private int stock;

    private double AverageRating = 0;

    private double sumOfRatings = 0;

    @DBRef
    @JsonIgnore
    private List<ReviewEntity> reviews;

    private List<String> size= new ArrayList<>();

}
