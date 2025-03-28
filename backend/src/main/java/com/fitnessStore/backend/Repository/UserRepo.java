package com.fitnessStore.backend.Repository;

import com.fitnessStore.backend.Entity.UserEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepo extends MongoRepository<UserEntity,ObjectId> {
    UserEntity findByEmail(String email);

    UserEntity findByUsername(String userName);
}
