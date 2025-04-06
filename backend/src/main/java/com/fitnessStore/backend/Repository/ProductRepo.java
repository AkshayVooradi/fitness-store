package com.fitnessStore.backend.Repository;

import com.fitnessStore.backend.Entity.ProductEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.security.core.parameters.P;

import java.util.List;


public interface ProductRepo extends MongoRepository<ProductEntity,String> {

    ProductEntity findByTitle(String title);

    List<ProductEntity> findByCategory(String category);

}
