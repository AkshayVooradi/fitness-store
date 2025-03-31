package com.fitnessStore.backend.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitnessStore.backend.Entity.UserEntity;
import com.fitnessStore.backend.ExceptionHandling.IncorrectToken;
import com.fitnessStore.backend.Services.UserServices;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserServices userServices;

    @Autowired
    private ObjectMapper objectMapper;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();


    @PostMapping("/user/signUp")
    public ResponseEntity<?> SignUp(@RequestBody UserEntity user){
        user.setPassword(encoder.encode(user.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setRole("USER");
        return userServices.SignUp(user);
    }

    @GetMapping("/user/login")
    public ResponseEntity<?> login(@RequestBody UserEntity user,HttpServletResponse response) throws IOException {
        return userServices.login(user);
    }

    @GetMapping("/admin/getUsers")
    public ResponseEntity<?> getAllUsers(HttpServletRequest request){
        return userServices.getAllUsers(request.getHeader("Authorization"));
    }

    @GetMapping("/user/orders")
    public ResponseEntity<?> getOrders(HttpServletRequest request){
        return userServices.getOrders(request.getHeader("Authorization"));
    }

    @GetMapping("/user/address")
    public ResponseEntity<?> getAddress(HttpServletRequest request){
        return userServices.getAddress(request.getHeader("Authorization"));
    }

    @GetMapping("/user/reviews")
    public ResponseEntity<?> getReviews(HttpServletRequest request){
        return userServices.getReviews(request.getHeader("Authorization"));
    }

    private void handleException(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        Map<String,String> errorResponse = new HashMap<>();
        errorResponse.put("status","401");
        errorResponse.put("error","unauthorized");
        errorResponse.put("message",message);

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
