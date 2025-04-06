package com.fitnessStore.backend.apiServices;

import com.fitnessStore.backend.Entity.UserEntity;
import com.fitnessStore.backend.Repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class GetUserByTokenTest {
    @Mock
    private JWTServices jwtServices;

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private GetUserByToken getUserByToken;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUserDetails_Success() {
        String token = "sampleToken";
        String email = "test@example.com";
        UserEntity user = UserEntity.builder().email(email).build();

        when(jwtServices.extractEmail(token)).thenReturn(email);
        when(userRepo.findByEmail(email)).thenReturn(Optional.of(user));

        UserEntity result = getUserByToken.userDetails(token);

        assertEquals(user, result);
    }

    @Test
    void testUserDetails_UserNotFound() {
        String token = "sampleToken";
        String email = "test@example.com";

        when(jwtServices.extractEmail(token)).thenReturn(email);
        when(userRepo.findByEmail(email)).thenReturn(Optional.empty());

        UserEntity result = getUserByToken.userDetails(token);

        assertEquals(null, result);
    }

}