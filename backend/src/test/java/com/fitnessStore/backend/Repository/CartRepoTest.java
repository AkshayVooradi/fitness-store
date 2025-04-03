package com.fitnessStore.backend.Repository;

import com.fitnessStore.backend.Entity.CartEntity;
import com.fitnessStore.backend.Entity.ProductEntity;
import com.fitnessStore.backend.Entity.UserEntity;
import com.fitnessStore.backend.StorageClasses.CartItemClass;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.parameters.P;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class CartRepoTest {

    @Autowired
    CartRepo cartRepo;

    @Autowired
    UserRepo userRepo; // Assuming you have a UserRepo to save UserEntity

    @BeforeEach
    void setUp() {
        UserEntity user = new UserEntity();
        user.setId(new ObjectId());
        user.setUsername("sample-tester");
        user.setEmail("sample-tester@example.com");
        userRepo.save(user); // Save the user first

        ProductEntity product1 = new ProductEntity();
        product1.setId(new ObjectId());
        product1.setTitle("Product 1");

        ProductEntity product2 = new ProductEntity();
        product2.setId(new ObjectId());
        product2.setTitle("Product 2");

        CartItemClass item1 = new CartItemClass();
        item1.setProduct(product1);
        item1.setQuantity(10);
        item1.setSize("S");
        item1.setCost(10.0);

        CartItemClass item2 = new CartItemClass();
        item2.setProduct(product2);
        item2.setQuantity(20);
        item2.setSize("M");
        item2.setCost(20.0);

        List<CartItemClass> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);

        CartEntity cart = CartEntity.builder().products(items).user(user).build();
        cartRepo.save(cart);
    }

    void tearDown(String email,ObjectId id){
        cartRepo.deleteById(id);
        userRepo.deleteByEmail(email);
    }

    @Test
    void findByUser() {
        UserEntity user = userRepo.findByUsername("sample-tester"); // Retrieve the user
        CartEntity cartResponse = cartRepo.findByUser(user);

        assertNotNull(cartResponse, "Cart should not be null");
        assertEquals("sample-tester", cartResponse.getUser().getUsername());
        assertEquals(2, cartResponse.getProducts().size());

        tearDown(user.getEmail(),cartResponse.getId());
    }
}