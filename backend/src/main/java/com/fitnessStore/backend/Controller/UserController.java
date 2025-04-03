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
import java.util.Arrays;
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
    public ResponseEntity<?> SignUp(@RequestBody Map<String,String> credentials){
        return userServices.SignUp(credentials.get("userName"),credentials.get("email"),credentials.get("password"));
    }

    @PostMapping("/user/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials,
                                   HttpServletResponse response) throws IOException {
        return userServices.login(credentials.get("email"),credentials.get("password"),response);

    }

    @PostMapping("/user/logout")
    public ResponseEntity<?> logout(HttpServletResponse response){
        System.out.println(true);
        return userServices.logout(response);
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

    @GetMapping("/auth/check-auth")
    public ResponseEntity<?> checkAuth(HttpServletRequest request,@CookieValue(value = "token", defaultValue = "") String token){

        if(token.isEmpty()){

            Map<String,Object> responseBody=new HashMap<>();
            responseBody.put("success",false);
            responseBody.put("message","Unauthorized user");

            return new ResponseEntity<>(responseBody,HttpStatus.UNAUTHORIZED);
        }

        return userServices.checkAuth(token);
    }
}
