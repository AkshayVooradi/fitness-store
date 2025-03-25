package com.fitnessStore.backend.Repository;

import com.fitnessStore.backend.Entity.CartEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CartRepo extends MongoRepository<CartEntity,ObjectId> {
}
