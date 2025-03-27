package com.fitnessStore.backend.Services;

import com.fitnessStore.backend.Entity.UserEntity;
import com.fitnessStore.backend.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class GetUserByToken {

    @Autowired
    private JWTServices jwtServices;

    @Autowired
    private UserRepo userRepo;

    public UserEntity userDetails(String authHeader){
        String token = null;
        String email = null;

        token = authHeader.substring(7);
        email = jwtServices.extractEmail(token);

        return userRepo.findByEmail(email);
    }
}
