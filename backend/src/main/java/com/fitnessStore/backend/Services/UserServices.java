package com.fitnessStore.backend.Services;

import com.fitnessStore.backend.Entity.OrderEntity;
import com.fitnessStore.backend.Entity.ReviewEntity;
import com.fitnessStore.backend.Entity.UserEntity;
import com.fitnessStore.backend.Repository.UserRepo;
import com.fitnessStore.backend.StorageClasses.AddressClass;
import com.fitnessStore.backend.apiServices.GetUserByToken;
import com.fitnessStore.backend.apiServices.JWTServices;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

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

    public ResponseEntity<?> SignUp(String userName,String email,String password){
        UserEntity user = UserEntity.builder()
                .username(userName)
                .email(email)
                .password(new BCryptPasswordEncoder().encode(password))
                .createdAt(LocalDateTime.now())
                .role("user")
                .build();
        Map<String,Object> responseBody = new HashMap<>();

        if(userRepo.findByEmail(email).isPresent()){
            responseBody.put("success",false);
            responseBody.put("message","User already exists with this mail");
            return new ResponseEntity<>(responseBody,HttpStatus.OK);
        }

        userRepo.save(user);

        responseBody.put("success",true);
        responseBody.put("message","Registration successful");
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    public ResponseEntity<?> login(String email, String password, HttpServletResponse response) {
        try {
            Authentication authentication = manager.authenticate(new UsernamePasswordAuthenticationToken(
                    email, password
            ));

            String token = jwtServices.generateToken(email);

            UserEntity user = userByToken.userDetails(token);

            Cookie cookie = new Cookie("token", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(false);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60);

            response.addCookie(cookie);

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("success", true);
            responseBody.put("message", "Logged in successfully");
            responseBody.put("user", Map.of(
                    "email", email,
                    "role", user.getRole(),
                    "id", user.getId(),
                    "userName", user.getUsername()
            ));

            return new ResponseEntity<>(responseBody, HttpStatus.OK);

        }catch (AuthenticationException e){
            System.out.println("Invalid credentials");
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("success", false);
            responseBody.put("message", "Incorrect username or password");
            return new ResponseEntity<>(responseBody,HttpStatus.OK);
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

    public ResponseEntity<?> checkAuth(String token) {
        Map<String,Object> responseBody = new HashMap<>();

        UserEntity user = userByToken.userDetails(token);

        if(user == null){
            responseBody.put("success",false);
            responseBody.put("message","Unauthorized user");
            return new ResponseEntity<>(responseBody,HttpStatus.UNAUTHORIZED);
        }

        responseBody.put("success",true);
        responseBody.put("message","Authenticated user");
        responseBody.put("user", Map.of(
                "email", user.getEmail(),
                "role", user.getRole(),
                "id", user.getId(),
                "userName", user.getUsername()
        ));

        return new ResponseEntity<>(responseBody,HttpStatus.OK);

    }

    public ResponseEntity<?> logout(HttpServletResponse response) {

        Cookie cookie = new Cookie("token", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        response.addCookie(cookie);

        Map<String,Object> responseBody = new HashMap<>();
        responseBody.put("success",true);
        responseBody.put("message","Logged out successfully!");

        return new ResponseEntity<>(responseBody,HttpStatus.OK);
    }
}
