package com.fitnessStore.backend.Repository;

import com.fitnessStore.backend.Entity.AddressEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AddressRepo extends MongoRepository<AddressEntity,ObjectId> {
}
