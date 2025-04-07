package com.fitnessStore.backend.Repository;

import com.fitnessStore.backend.Entity.ProductEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class ProductRepoCustomTest {

    @Autowired
    ProductRepo productRepo;

    @Autowired
    ProductRepoCustom productRepoCustom;

    @BeforeEach
    void setUp(){
        ProductEntity p1 = ProductEntity.builder()
                .title("Sample Dumbell")
                .description("A high-quality dumbell")
                .brand("FitnessBrand")
                .category("Fitness")
                .price(100.0)
                .build();
        productRepo.save(p1);

        ProductEntity p2 = ProductEntity.builder()
                .title("Sample Shoes")
                .description("Comfortable and stylish footwear")
                .brand("FootwearBrand")
                .category("Footwear")
                .price(200.0)
                .build();
        productRepo.save(p2);
    }

    @AfterEach
    void tearDown(){
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
        List<ProductEntity> results = productRepoCustom.filterProducts(List.of("Footwear"), null, "title-atoz");
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("Sample Shoes", results.get(0).getTitle());
    }
}