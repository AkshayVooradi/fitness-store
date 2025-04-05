package com.fitnessStore.backend.Repository;

import com.fitnessStore.backend.Entity.UserEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepo extends MongoRepository<UserEntity,String> {
    Optional<UserEntity> findByEmail(String email);

    UserEntity findByUsername(String userName);
}
