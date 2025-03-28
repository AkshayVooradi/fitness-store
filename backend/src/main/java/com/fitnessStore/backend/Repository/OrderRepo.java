package com.fitnessStore.backend.Repository;

import com.fitnessStore.backend.Entity.OrderEntity;
import org.bson.types.ObjectId;
import org.springframework.core.annotation.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface OrderRepo extends MongoRepository<OrderEntity,ObjectId> {
}
