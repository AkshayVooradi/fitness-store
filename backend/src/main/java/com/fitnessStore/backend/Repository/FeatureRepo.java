package com.fitnessStore.backend.Repository;

import com.fitnessStore.backend.Entity.FeatureEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FeatureRepo extends MongoRepository<FeatureEntity, ObjectId> {
}
