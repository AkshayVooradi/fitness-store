package com.fitnessStore.backend.Repository;

import com.fitnessStore.backend.Entity.ReviewEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReviewRepo extends MongoRepository<ReviewEntity,ObjectId> {
}
