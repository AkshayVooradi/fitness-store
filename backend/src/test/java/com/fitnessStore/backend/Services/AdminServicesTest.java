package com.fitnessStore.backend.Services;

import com.fitnessStore.backend.Entity.OrderEntity;
import com.fitnessStore.backend.Entity.ProductEntity;
import com.fitnessStore.backend.Entity.UserEntity;
import com.fitnessStore.backend.Repository.OrderRepo;
import com.fitnessStore.backend.Repository.ProductRepo;
import com.fitnessStore.backend.Repository.UserRepo;
import com.fitnessStore.backend.apiServices.GetUserByToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class AdminServicesTest {

    @Mock
    private ProductRepo productRepo;

    @Mock
    private GetUserByToken userByToken;

    @Mock
    private UserRepo userRepo;

    @Mock
    private OrderRepo orderRepo;

    @Mock
    private CloudinaryService cloudinaryService;

    @InjectMocks
    private AdminServices adminServices;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddProduct_Success() {
        UserEntity user = UserEntity.builder().role("admin").build();
        when(userByToken.userDetails(anyString())).thenReturn(user);

        ResponseEntity<?> response = adminServices.addProduct("title", "category", "brand", "100", "90", "description", "10","img_url","token");

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(true, ((Map<String, Object>) response.getBody()).get("success"));
    }

    @Test
    void testAddProduct_UnauthorizedUser() {
        UserEntity user = UserEntity.builder().role("user").build();
        when(userByToken.userDetails(anyString())).thenReturn(user);

        ResponseEntity<?> response = adminServices.addProduct("title", "category", "brand", "100", "90", "description", "10", "img_url","token");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(false, ((Map<String, Object>) response.getBody()).get("success"));
        assertEquals("Unauthorized user", ((Map<String, Object>) response.getBody()).get("message"));
    }

    @Test
    void testDeleteProduct_Success() {
        UserEntity user = UserEntity.builder().role("admin").build();
        when(userByToken.userDetails(anyString())).thenReturn(user);
        when(productRepo.findById(anyString())).thenReturn(Optional.of(new ProductEntity()));

        ResponseEntity<?> response = adminServices.deleteProduct("productId", "token");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, ((Map<String, Object>) response.getBody()).get("success"));
    }

    @Test
    void testDeleteProduct_UnauthorizedUser() {
        UserEntity user = UserEntity.builder().role("user").build();
        when(userByToken.userDetails(anyString())).thenReturn(user);

        ResponseEntity<?> response = adminServices.deleteProduct("productId", "token");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(false, ((Map<String, Object>) response.getBody()).get("success"));
        assertEquals("Unauthorized user", ((Map<String, Object>) response.getBody()).get("message"));
    }

    @Test
    void testGetAllProducts_Success() {
        UserEntity user = UserEntity.builder().role("admin").build();
        when(userByToken.userDetails(anyString())).thenReturn(user);

        ResponseEntity<?> response = adminServices.getAllProducts("token");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, ((Map<String, Object>) response.getBody()).get("success"));
    }

    @Test
    void testGetAllProducts_UnauthorizedUser() {
        UserEntity user = UserEntity.builder().role("user").build();
        when(userByToken.userDetails(anyString())).thenReturn(user);

        ResponseEntity<?> response = adminServices.getAllProducts("token");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(false, ((Map<String, Object>) response.getBody()).get("success"));
        assertEquals("Unauthorized user", ((Map<String, Object>) response.getBody()).get("message"));
    }

    @Test
    void testUpdateProduct_Success() {
        UserEntity user = UserEntity.builder().role("admin").build();
        ProductEntity product = ProductEntity.builder().id("productId").build();
        when(userByToken.userDetails(anyString())).thenReturn(user);
        when(productRepo.findById(anyString())).thenReturn(Optional.of(product));

        ResponseEntity<?> response = adminServices.updateProduct("productId", "title", "category", "brand", "100", "90", "description", "10", "token");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, ((Map<String, Object>) response.getBody()).get("success"));
    }

    @Test
    void testUpdateProduct_UnauthorizedUser() {
        UserEntity user = UserEntity.builder().role("user").build();
        when(userByToken.userDetails(anyString())).thenReturn(user);

        ResponseEntity<?> response = adminServices.updateProduct("productId", "title", "category", "brand", "100", "90", "description", "10", "token");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(false, ((Map<String, Object>) response.getBody()).get("success"));
        assertEquals("Unauthorized user", ((Map<String, Object>) response.getBody()).get("message"));
    }

    @Test
    void testUpdateOrderById_Success() {
        UserEntity user = UserEntity.builder().role("admin").build();
        OrderEntity order = OrderEntity.builder().id("orderId").build();
        when(userByToken.userDetails(anyString())).thenReturn(user);
        when(orderRepo.findById(anyString())).thenReturn(Optional.of(order));

        ResponseEntity<?> response = adminServices.updateOrderById("orderId", "token", "Shipped");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, ((Map<String, Object>) response.getBody()).get("success"));
        assertEquals("Order status is updated successfully!", ((Map<String, Object>) response.getBody()).get("message"));
    }

    @Test
    void testUpdateOrderById_UnauthorizedUser() {
        UserEntity user = UserEntity.builder().role("user").build();
        when(userByToken.userDetails(anyString())).thenReturn(user);

        ResponseEntity<?> response = adminServices.updateOrderById("orderId", "token", "Shipped");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(false, ((Map<String, Object>) response.getBody()).get("success"));
        assertEquals("Unauthorized user", ((Map<String, Object>) response.getBody()).get("message"));
    }

    @Test
    void testGetOrderById_Success() {
        UserEntity user = UserEntity.builder().role("admin").build();
        OrderEntity order = OrderEntity.builder().id("orderId").build();
        when(userByToken.userDetails(anyString())).thenReturn(user);
        when(orderRepo.findById(anyString())).thenReturn(Optional.of(order));

        ResponseEntity<?> response = adminServices.getOrderById("orderId", "token");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, ((Map<String, Object>) response.getBody()).get("success"));
        assertEquals(Optional.of(order), ((Map<String, Object>) response.getBody()).get("data"));
    }

    @Test
    void testGetOrderById_UnauthorizedUser() {
        UserEntity user = UserEntity.builder().role("user").build();
        when(userByToken.userDetails(anyString())).thenReturn(user);

        ResponseEntity<?> response = adminServices.getOrderById("orderId", "token");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(false, ((Map<String, Object>) response.getBody()).get("success"));
        assertEquals("Unauthorized user", ((Map<String, Object>) response.getBody()).get("message"));
    }
}
