package com.fitnessStore.backend.Controller;

import com.fitnessStore.backend.Services.UserServices;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserControllerTest {
    @Mock
    private UserServices userServices;

    @InjectMocks
    private UserController userController;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSignUp() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("userName", "testUser");
        credentials.put("email", "test@example.com");
        credentials.put("password", "password");

        when(userServices.SignUp(anyString(), anyString(), anyString()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> result = userController.SignUp(credentials);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(userServices, times(1)).SignUp("testUser", "test@example.com", "password");
    }

    @Test
    public void testLogin() throws IOException {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("email", "test@example.com");
        credentials.put("password", "password");

        when(userServices.login(anyString(), anyString(), any(HttpServletResponse.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> result = userController.login(credentials, response);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(userServices, times(1)).login("test@example.com", "password", response);
    }

    @Test
    public void testLogout() {
        when(userServices.logout(any(HttpServletResponse.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> result = userController.logout(response);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(userServices, times(1)).logout(response);
    }

    @Test
    public void testGetAllUsers() {
        when(request.getHeader("Authorization")).thenReturn("Bearer token");
        when(userServices.getAllUsers(anyString()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> result = userController.getAllUsers(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(userServices, times(1)).getAllUsers("Bearer token");
    }

    @Test
    public void testGetAddress() {
        when(request.getHeader("Authorization")).thenReturn("Bearer token");
        when(userServices.getAddress(anyString()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> result = userController.getAddress(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(userServices, times(1)).getAddress("Bearer token");
    }

    @Test
    public void testGetReviews() {
        when(request.getHeader("Authorization")).thenReturn("Bearer token");
        when(userServices.getReviews(anyString()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> result = userController.getReviews(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(userServices, times(1)).getReviews("Bearer token");
    }

    @Test
    public void testCheckAuth() {
        when(request.getHeader("Authorization")).thenReturn("Bearer token");
        when(userServices.checkAuth(anyString()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> result = userController.checkAuth(request, "token");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(userServices, times(1)).checkAuth("token");
    }

}