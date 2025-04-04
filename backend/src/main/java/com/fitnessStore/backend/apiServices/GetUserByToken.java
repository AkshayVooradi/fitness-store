package com.fitnessStore.backend.apiServices;

import com.fitnessStore.backend.Entity.UserEntity;
import com.fitnessStore.backend.Repository.ProductRepo;
import com.fitnessStore.backend.Repository.UserRepo;
import com.fitnessStore.backend.StorageClasses.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class GetUserByToken {

    @Autowired
    private JWTServices jwtServices;

    @Autowired
    private UserRepo userRepo;

    public UserEntity userDetails(String authHeader){
        String token = null;
        String email = null;

        token = authHeader;
        email = jwtServices.extractEmail(token);

        Optional<UserEntity> user = userRepo.findByEmail(email);

        return user.orElse(null);

    }
}
