package com.fitnessStore.backend.Services;

import com.fitnessStore.backend.Entity.UserEntity;
import com.fitnessStore.backend.Repository.UserRepo;
import com.fitnessStore.backend.apiServices.GetUserByToken;
import com.fitnessStore.backend.apiServices.JWTServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserServices {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JWTServices jwtServices;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private GetUserByToken userByToken;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public ResponseEntity<?> SignUp(UserEntity user){
        return new ResponseEntity<>(userRepo.save(user), HttpStatus.CREATED);
    }

    public ResponseEntity<?> login(UserEntity user) {
        Authentication authentication = manager.authenticate(new UsernamePasswordAuthenticationToken(
                user.getEmail(),user.getPassword()
        ));


        return authentication.isAuthenticated() ?
                new ResponseEntity<>(jwtServices.generateToken(user.getEmail()),HttpStatus.ACCEPTED)
                : new ResponseEntity<>("Unauthorized User",HttpStatus.UNAUTHORIZED);
    }

    public ResponseEntity<?> getAllUsers(String authHeader) {
        UserEntity user = userByToken.userDetails(authHeader);

        if(!user.getRole().equals("ADMIN")){
            return new ResponseEntity<>("Unauthorized User",HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(userRepo.findAll(),HttpStatus.OK);
    }
}
