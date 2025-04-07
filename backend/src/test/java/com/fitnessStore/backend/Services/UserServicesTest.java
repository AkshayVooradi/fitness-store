package com.fitnessStore.backend.Services;

import static org.junit.jupiter.api.Assertions.*;

import com.fitnessStore.backend.Entity.*;
import com.fitnessStore.backend.Repository.CartRepo;
import com.fitnessStore.backend.Repository.OrderRepo;
import com.fitnessStore.backend.Repository.ProductRepo;
import com.fitnessStore.backend.Repository.UserRepo;
import com.fitnessStore.backend.StorageClasses.AddressClass;
import com.fitnessStore.backend.StorageClasses.CartItemClass;
import com.fitnessStore.backend.apiServices.GetUserByToken;
import com.fitnessStore.backend.apiServices.JWTServices;
import jakarta.servlet.http.HttpServletResponse;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServicesTest {
    @InjectMocks
    private UserServices userServices;

    @Mock
    private UserRepo userRepo;

    @Mock
    private JWTServices jwtServices;

    @Mock
    private AuthenticationManager manager;

    @Mock
    private GetUserByToken userByToken;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSignUp_UserAlreadyExists() {
        String userName = "testUser";
        String email = "test@example.com";
        String password = "password";
        when(userRepo.findByEmail(email)).thenReturn(Optional.of(new UserEntity()));

        ResponseEntity<?> response = userServices.SignUp(userName, email, password);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse((Boolean) ((Map<String, Object>) response.getBody()).get("success"));
        assertEquals("User already exists with this mail", ((Map<String, Object>) response.getBody()).get("message"));
    }

    @Test
    void testSignUp_Success() {
        String userName = "testUser";
        String email = "test@example.com";
        String password = "password";
        when(userRepo.findByEmail(email)).thenReturn(Optional.empty());

        ResponseEntity<?> response = userServices.SignUp(userName, email, password);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) ((Map<String, Object>) response.getBody()).get("success"));
        assertEquals("Registration successful", ((Map<String, Object>) response.getBody()).get("message"));
    }

    @Test
    void testLogin_InvalidCredentials() {
        String email = "test@example.com";
        String password = "password";
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(manager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(BadCredentialsException.class);

        ResponseEntity<?> result = userServices.login(email, password, response);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertFalse((Boolean) ((Map<String, Object>) result.getBody()).get("success"));
        assertEquals("Incorrect username or password", ((Map<String, Object>) result.getBody()).get("message"));
    }

    @Test
    void testLogin_Success() {
        String email = "test@example.com";
        String password = "password";
        HttpServletResponse response = mock(HttpServletResponse.class);
        Authentication authentication = mock(Authentication.class);
        when(manager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtServices.generateToken(email)).thenReturn("token");
        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setRole("user");
        user.setId("userId");
        user.setUsername("testUser");
        when(userByToken.userDetails("token")).thenReturn(user);

        ResponseEntity<?> result = userServices.login(email, password, response);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue((Boolean) ((Map<String, Object>) result.getBody()).get("success"));
        assertEquals("Logged in successfully", ((Map<String, Object>) result.getBody()).get("message"));
    }

    @Test
    void testGetAllUsers_Unauthorized() {
        String authHeader = "authToken";
        UserEntity user = new UserEntity();
        user.setRole("user");
        when(userByToken.userDetails(authHeader)).thenReturn(user);

        ResponseEntity<?> response = userServices.getAllUsers(authHeader);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Unauthorized User", response.getBody());
    }

    @Test
    void testGetAllUsers_Success() {
        String authHeader = "authToken";
        UserEntity user = new UserEntity();
        user.setRole("ADMIN");
        when(userByToken.userDetails(authHeader)).thenReturn(user);
        List<UserEntity> users = new ArrayList<>();
        when(userRepo.findAll()).thenReturn(users);

        ResponseEntity<?> response = userServices.getAllUsers(authHeader);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
    }

    @Test
    void testGetAddress_NoAddressFound() {
        String authorization = "authToken";
        UserEntity user = new UserEntity();
        user.setAddress(new HashSet<>());
        when(userByToken.userDetails(authorization)).thenReturn(user);

        ResponseEntity<?> response = userServices.getAddress(authorization);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No address Found", response.getBody());
    }

    @Test
    void testGetAddress_Success() {
        String authorization = "authToken";
        UserEntity user = new UserEntity();
        Set<AddressClass> address = new HashSet<>();
        address.add(new AddressClass());
        user.setAddress(address);
        when(userByToken.userDetails(authorization)).thenReturn(user);

        ResponseEntity<?> response = userServices.getAddress(authorization);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(address, response.getBody());
    }

    @Test
    void testGetReviews_NoReviewsFound() {
        String authorization = "authToken";
        UserEntity user = new UserEntity();
        user.setReviews(new ArrayList<>());
        when(userByToken.userDetails(authorization)).thenReturn(user);

        ResponseEntity<?> response = userServices.getReviews(authorization);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No Reviews Found", response.getBody());
    }

    @Test
    void testGetReviews_Success() {
        String authorization = "authToken";
        UserEntity user = new UserEntity();
        List<ReviewEntity> reviews = new ArrayList<>();
        reviews.add(new ReviewEntity());
        user.setReviews(reviews);
        when(userByToken.userDetails(authorization)).thenReturn(user);

        ResponseEntity<?> response = userServices.getReviews(authorization);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reviews, response.getBody());
    }

    @Test
    void testCheckAuth_Unauthorized() {
        String token = "authToken";
        when(userByToken.userDetails(token)).thenReturn(null);

        ResponseEntity<?> response = userServices.checkAuth(token);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertFalse((Boolean) ((Map<String, Object>) response.getBody()).get("success"));
        assertEquals("Unauthorized user", ((Map<String, Object>) response.getBody()).get("message"));
    }

    @Test
    void testCheckAuth_Success() {
        String token = "authToken";
        UserEntity user = new UserEntity();
        user.setEmail("test@example.com");
        user.setRole("user");
        user.setId("userId");
        user.setUsername("testUser");
        when(userByToken.userDetails(token)).thenReturn(user);

        ResponseEntity<?> response = userServices.checkAuth(token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) ((Map<String, Object>) response.getBody()).get("success"));
        assertEquals("Authenticated user", ((Map<String, Object>) response.getBody()).get("message"));
    }

    @Test
    void testLogout_Success() {
        HttpServletResponse response = mock(HttpServletResponse.class);

        ResponseEntity<?> result = userServices.logout(response);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue((Boolean) ((Map<String, Object>) result.getBody()).get("success"));
        assertEquals("Logged out successfully!", ((Map<String, Object>) result.getBody()).get("message"));
    }
}