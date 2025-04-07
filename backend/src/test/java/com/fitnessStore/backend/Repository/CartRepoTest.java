package com.fitnessStore.backend.Repository;

import com.fitnessStore.backend.Entity.CartEntity;
import com.fitnessStore.backend.Entity.ProductEntity;
import com.fitnessStore.backend.Entity.UserEntity;
import com.fitnessStore.backend.StorageClasses.CartItemClass;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@ActiveProfiles("test")
class CartRepoTest {

    @Autowired
    CartRepo cartRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    ProductRepo productRepo;

    private UserEntity user;
    private CartEntity cart;

    @BeforeEach
    void setUp() {
        user = UserEntity.builder()
                .username("testUser")
                .email("test@example.com")
                .mobile("999999999")
                .role("USER")
                .build();
        userRepo.save(user);

        ProductEntity product = ProductEntity.builder()
                .title("Sample Product")
                .category("Category")
                .description("Sample Description")
                .image("sample.jpg")
                .totalStock(10)
                .build();
        productRepo.save(product);

        CartItemClass cartItem = CartItemClass.builder()
                .product(product)
                .productId(product.getId())
                .quantity(2)
                .title("Sample Product")
                .price(100.0)
                .salePrice(90.0)
                .build();

        cart = CartEntity.builder()
                .user(user)
                .products(List.of(cartItem))
                .build();
        cartRepo.save(cart);
    }

    @AfterEach
    void tearDown() {
        cartRepo.deleteAll();
        userRepo.deleteAll();
        productRepo.deleteAll();
    }

    @Test
    void findByUser() {
        CartEntity foundCart = cartRepo.findByUser(user);
        assertNotNull(foundCart);
        assertEquals(1, foundCart.getProducts().size());
        assertEquals("Sample Product", foundCart.getProducts().get(0).getTitle());
    }
}