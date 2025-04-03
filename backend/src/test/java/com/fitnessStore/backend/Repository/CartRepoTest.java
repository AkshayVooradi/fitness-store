package com.fitnessStore.backend.Repository;

import com.fitnessStore.backend.Entity.ProductEntity;
import com.fitnessStore.backend.Entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CartRepoTest {

    @Autowired
    UserRepo userRepo;
    @Autowired
    ProductRepo productRepo;

    @Test
    void findByUser() {
        UserEntity user = new UserEntity();
        user.setEmail("test@example.com");
        user.setUsername("testUser");
        userRepo.save(user);


        ProductEntity product=new ProductEntity();
        product.setTitle("White T-Shirt");
        product.setBrand("Sample Brand");
        product.setCategory("Apparels");
        product.setDiscountPercent(15);
        productRepo.save(product);
    }
}