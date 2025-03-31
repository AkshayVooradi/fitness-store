package com.fitnessStore.backend.Services;

import com.fitnessStore.backend.Entity.OrderEntity;
import com.fitnessStore.backend.Entity.ReviewEntity;
import com.fitnessStore.backend.Entity.UserEntity;
import com.fitnessStore.backend.ExceptionHandling.IncorrectToken;
import com.fitnessStore.backend.Repository.UserRepo;
import com.fitnessStore.backend.StorageClasses.AddressClass;
import com.fitnessStore.backend.apiServices.GetUserByToken;
import com.fitnessStore.backend.apiServices.JWTServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

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
        user.setRole("USER");
        return new ResponseEntity<>(userRepo.save(user), HttpStatus.CREATED);
    }

    public ResponseEntity<?> login(UserEntity user) {
        try {
            Authentication authentication = manager.authenticate(new UsernamePasswordAuthenticationToken(
                    user.getEmail(), user.getPassword()
            ));

            return new ResponseEntity<>(jwtServices.generateToken(user.getEmail()), HttpStatus.ACCEPTED);

        }catch (AuthenticationException e){
            System.out.println("Invalid credentials");
            return new ResponseEntity<>("Invalid Username or password",HttpStatus.UNAUTHORIZED);
        }
    }

    public ResponseEntity<?> getAllUsers(String authHeader) {
        UserEntity user = userByToken.userDetails(authHeader);

        if(!user.getRole().equals("ADMIN")){
            return new ResponseEntity<>("Unauthorized User",HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(userRepo.findAll(),HttpStatus.OK);
    }

    public ResponseEntity<?> getOrders(String authorization) {
        UserEntity user = userByToken.userDetails(authorization);

        List<OrderEntity> orders = user.getOrders();

        if(orders.isEmpty()){
            return new ResponseEntity<>("No previous Orders",HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(orders,HttpStatus.OK);
    }

    public ResponseEntity<?> getAddress(String authorization) {
        UserEntity user = userByToken.userDetails(authorization);

        Set<AddressClass> address = user.getAddress();

        if(address.isEmpty()){
            return new ResponseEntity<>("No address Found",HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(address,HttpStatus.OK);
    }

    public ResponseEntity<?> getReviews(String authorization){
        UserEntity user = userByToken.userDetails(authorization);

        List<ReviewEntity> reviews = user.getReviews();

        if(reviews.isEmpty()){
            return new ResponseEntity<>("No Reviews Found", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(user.getReviews(),HttpStatus.OK);
    }
}
