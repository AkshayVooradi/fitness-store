package com.fitnessStore.backend.Repository;

import com.fitnessStore.backend.StorageClasses.AddressClass;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AddressRepo extends MongoRepository<AddressClass,ObjectId> {
}
