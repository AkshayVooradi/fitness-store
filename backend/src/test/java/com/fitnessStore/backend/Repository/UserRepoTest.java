package com.fitnessStore.backend.Repository;

import com.fitnessStore.backend.Entity.UserEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class UserRepoTest {

    @Autowired
    UserRepo userRepo;


    void tearDown(String email){
        userRepo.deleteByEmail(email);
    }

    @Test
    void findByEmail() {
        UserEntity user = new UserEntity();
        user.setEmail("sample@sample.com");
        user.setUsername("sampleUser");
        userRepo.save(user);

        UserEntity foundUser = userRepo.findByEmail("sample@sample.com");
        assertNotNull(foundUser);
        assertEquals("sampleUser", foundUser.getUsername());

        tearDown(user.getEmail());
    }

    @Test
    void findByUsername() {
        UserEntity user = new UserEntity();
        user.setEmail("test@example.com");
        user.setUsername("testUser");
        userRepo.save(user);

        UserEntity foundUser = userRepo.findByUsername("testUser");
        assertNotNull(foundUser);
        assertEquals("test@example.com", foundUser.getEmail());

        tearDown(user.getEmail());
    }
}
