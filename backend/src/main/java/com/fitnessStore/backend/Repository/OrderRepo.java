package com.fitnessStore.backend.Repository;

import com.fitnessStore.backend.Entity.OrderEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepo extends MongoRepository<OrderEntity,ObjectId> {
}
