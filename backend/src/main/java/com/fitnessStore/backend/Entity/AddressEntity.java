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

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "address_entity")
public class AddressEntity {
    @Id
    private ObjectId id;

    private String streetAddress;

    private String city;

    private String state;

    private String zipcode;

    @DBRef
    @JsonIgnore
    @Field("user_id")
    private UserEntity user;
}
