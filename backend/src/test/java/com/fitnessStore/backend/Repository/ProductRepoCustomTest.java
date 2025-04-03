package com.fitnessStore.backend.Repository;

import com.fitnessStore.backend.Entity.ProductEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.parameters.P;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ProductRepoCustomTest {

    @Autowired
    ProductRepo productRepo;

    @Autowired
    ProductRepoCustom productRepoCustom;

    @BeforeEach
     void setUp(){
        ProductEntity p1=new ProductEntity();
        p1.setTitle("Sample Dumbell");
        p1.setCategory("Fitness");
        p1.setPrice(100.0);
        p1.setDiscountPercent(10);
        p1.setAverageRating(4.5);

        productRepo.save(p1);

        ProductEntity p2=new ProductEntity();
        p2.setTitle("Sample Shoes");
        p2.setCategory("Footwear");
        p2.setPrice(200.0);
        p2.setDiscountPercent(20);
        p2.setAverageRating(3.5);

        productRepo.save(p2);
    }

    @AfterEach
    void clearValues(){
        productRepo.deleteAll();
    }


    @Test
    void searchProducts() {
        List<ProductEntity> results=productRepoCustom.searchProducts("Sample");
        assertNotNull(results);
        assertEquals(2, results.size());
    }

    @Test
    void filterProducts() {
        List<ProductEntity> results = productRepoCustom.filterProducts("Footwear",null,null,null,null);
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("Sample Shoes", results.get(0).getTitle());
    }
}