package com.fitnessStore.backend.Repository;

import com.fitnessStore.backend.Entity.UserEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserRepoTest {

    @Autowired
    UserRepo userRepo;

    @AfterEach
    void tearDown(){
        userRepo.deleteAll();
    }

    @BeforeEach
    void setup(){
        UserEntity user = UserEntity.builder()
                .username("testUser")
                .email("test@example.com")
                .mobile("999999999")
                .role("USER")
                .build();
        userRepo.save(user);
    }

    @Test
    void findByEmail() {
        Optional<UserEntity> foundUser = userRepo.findByEmail("test@example.com");
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("testUser");
    }

    @Test
    void findByUsername() {
        UserEntity foundUser = userRepo.findByUsername("testUser");
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getEmail()).isEqualTo("test@example.com");
    }
}