package com.fitnessStore.backend.Repository;

import com.fitnessStore.backend.Entity.ProductEntity;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductRepoTest {

    @Autowired
    ProductRepo productRepo;

    void tearDown(ObjectId id){
        productRepo.deleteById(id);
    }
    @Test
    void findByTitle() {
        ProductEntity product=new ProductEntity();
        product.setTitle("White T-Shirt");
        product.setBrand("Sample Brand");
        product.setCategory("Apparels");
        product.setDiscountPercent(15);
        productRepo.save(product);

        ProductEntity foundProduct = productRepo.findByTitle("White T-Shirt");
        assertNotNull(foundProduct);
        assertEquals("Sample Brand", foundProduct.getBrand());
        assertEquals(15,foundProduct.getDiscountPercent());

       tearDown(product.getId());

    }

    @Test
    void findByCategory() {
        ProductEntity product = new ProductEntity();
        product.setTitle("Test Product");
        product.setCategory("Fitness");
        product.setBrand("Test Brand");
        product.setPrice(200.0);
        product.setDescription("Test Description");
        productRepo.save(product);

        List<ProductEntity> foundProducts = productRepo.findByCategory("Fitness");
        assertFalse(foundProducts.isEmpty());
        assertEquals("Test Brand", foundProducts.get(0).getBrand());

        tearDown(product.getId());
    }
}
