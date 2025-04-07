package com.fitnessStore.backend.apiServices;

import com.fitnessStore.backend.Entity.UserEntity;
import com.fitnessStore.backend.Repository.UserRepo;
import com.fitnessStore.backend.StorageClasses.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class MyUserDetailsServicesTest {
    @InjectMocks
    private MyUserDetailsServices myUserDetailsServices;

    @Mock
    UserRepo userRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadUserByUsername_UserFound() {
        String email = "test@example.com";
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(email);
        when(userRepo.findByEmail(email)).thenReturn(Optional.of(userEntity));

        UserPrincipal userPrincipal = (UserPrincipal) myUserDetailsServices.loadUserByUsername(email);

        assertNotNull(userPrincipal);
        assertEquals(email, userPrincipal.getUsername());
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        String email = "notfound@example.com";
        when(userRepo.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> myUserDetailsServices.loadUserByUsername(email));
    }

}