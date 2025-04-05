package com.fitnessStore.backend.Repository;

import com.fitnessStore.backend.Entity.ReviewEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReviewRepo extends MongoRepository<ReviewEntity,ObjectId> {
    List<ReviewEntity> findByProductId(String id);
}
