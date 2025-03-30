package com.fitnessStore.backend.Repository;

import com.fitnessStore.backend.Entity.ProductEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface ProductRepo extends MongoRepository<ProductEntity,ObjectId> {

    ProductEntity findByTitle(String title);

    List<ProductEntity> findByCategory(String category);

}
