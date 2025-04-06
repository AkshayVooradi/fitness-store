package com.fitnessStore.backend.apiServices;


import com.fitnessStore.backend.ExceptionHandling.IncorrectToken;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class JWTServicesTest {

    @InjectMocks
    private JWTServices jwtServices;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtServices = new JWTServices();
    }

    @Test
    void testGenerateToken() {
        String email = "test@example.com";
        String token = jwtServices.generateToken(email);
        assertNotNull(token);
    }

    @Test
    void testExtractEmail() {
        String email = "test@example.com";
        String token = jwtServices.generateToken(email);
        String extractedEmail = jwtServices.extractEmail(token);
        assertEquals(email, extractedEmail);
    }

    @Test
    void testValidateToken() {
        String email = "test@example.com";
        String token = jwtServices.generateToken(email);
        when(userDetails.getUsername()).thenReturn(email);
        boolean isValid = jwtServices.validateToken(token, userDetails);
        assertTrue(isValid);
    }

    @Test
    void testIsTokenExpired() {
        String email = "test@example.com";
        String token = jwtServices.generateToken(email);
        boolean isExpired = jwtServices.isTokenExpired(token);
        assertFalse(isExpired);
    }

    @Test
    void testExtractExpiration() {
        String email = "test@example.com";
        String token = jwtServices.generateToken(email);
        Date expiration = jwtServices.extractExpiration(token);
        assertNotNull(expiration);
    }

    @Test
    void testExtractAllClaims() {
        String email = "test@example.com";
        String token = jwtServices.generateToken(email);
        Claims claims = jwtServices.extractAllClaims(token);
        assertNotNull(claims);
    }

    @Test
    void testIncorrectTokenHandling() {
        String invalidToken = "invalid.token";
        assertThrows(IncorrectToken.class, () -> jwtServices.extractAllClaims(invalidToken));
    }
}
