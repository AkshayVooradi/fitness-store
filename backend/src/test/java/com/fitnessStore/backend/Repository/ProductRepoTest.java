package com.fitnessStore.backend.Repository;

import com.fitnessStore.backend.Entity.ProductEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductRepoTest {

    @Autowired
    ProductRepo productRepo;

    @BeforeEach
    void setup(){
        ProductEntity p1=new ProductEntity();
        p1.setTitle("White T-Shirt");
        p1.setBrand("Sample Brand");
        p1.setCategory("Apparels");
        productRepo.save(p1);

        ProductEntity p2 = new ProductEntity();
        p2.setTitle("Test Product");
        p2.setCategory("Fitness");
        p2.setBrand("Test Brand");
        p2.setDescription("Test Description");
        productRepo.save(p2);
    }

    @AfterEach
    void tearDown(){
        productRepo.deleteAll();
    }

    @Test
    void findByTitle() {
        ProductEntity foundProduct = productRepo.findByTitle("White T-Shirt");
        assertNotNull(foundProduct);
        assertEquals("Sample Brand", foundProduct.getBrand());
    }

    @Test
    void findByCategory() {
        List<ProductEntity> foundProducts = productRepo.findByCategory("Fitness");
        assertFalse(foundProducts.isEmpty());
        assertEquals("Test Brand", foundProducts.get(0).getBrand());
    }
}