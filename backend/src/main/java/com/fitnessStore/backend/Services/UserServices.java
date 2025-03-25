package com.fitnessStore.backend.Services;

import com.fitnessStore.backend.Entity.UserEntity;
import com.fitnessStore.backend.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserServices {

    @Autowired
    private UserRepo userRepo;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public ResponseEntity<?> SignUp(UserEntity user){
        return new ResponseEntity<>(userRepo.save(user), HttpStatus.CREATED);
    }

    public ResponseEntity<?> login(String email, String password) {

        UserEntity user = userRepo.findByEmail(email);
        if(user == null){
            return new ResponseEntity<>("Email Not Found",HttpStatus.NOT_FOUND);
        }

        if(!encoder.matches(password,user.getPassword())){
            return new ResponseEntity<>("Password Doesn't match",HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>("Logged In Successfully",HttpStatus.ACCEPTED);

    }
}
