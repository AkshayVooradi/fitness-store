package com.fitnessStore.backend.Repository;

import com.fitnessStore.backend.Entity.OrderEntity;
import com.fitnessStore.backend.Entity.UserEntity;
import com.fitnessStore.backend.StorageClasses.CartItemClass;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class OrderRepoTest {

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    UserRepo userRepo;

    private UserEntity user;
    private OrderEntity order;

    @BeforeEach
    void setUp() {
        user = UserEntity.builder()
                .username("testUser")
                .email("test@example.com")
                .mobile("999999999")
                .role("USER")
                .build();
        userRepo.save(user);

        CartItemClass cartItem = CartItemClass.builder()
                .productId("sampleProductId")
                .quantity(2)
                .title("Sample Product")
                .price(50.0)
                .salePrice(45.0)
                .build();

        order = OrderEntity.builder()
                .user(user)
                .userId(user.getId())
                .cartItems(List.of(cartItem))
                .orderStatus("Pending")
                .orderDate(LocalDateTime.now())
                .orderUpdateDate(LocalDateTime.now())
                .totalAmount(90.0)
                .build();
        orderRepo.save(order);
    }

    @AfterEach
    void tearDown() {
        orderRepo.deleteAll();
        userRepo.deleteAll();
    }

    @Test
    void findByUserId() {
        List<OrderEntity> foundOrders = orderRepo.findByUserId(user.getId());
        assertNotNull(foundOrders);
        assertEquals(1, foundOrders.size());
        assertEquals("Pending", foundOrders.get(0).getOrderStatus());
    }
}