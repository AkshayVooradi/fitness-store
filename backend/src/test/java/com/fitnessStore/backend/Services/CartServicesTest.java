package com.fitnessStore.backend.Services;

import com.fitnessStore.backend.Entity.CartEntity;
import com.fitnessStore.backend.Entity.ProductEntity;
import com.fitnessStore.backend.Entity.UserEntity;
import com.fitnessStore.backend.Repository.CartRepo;
import com.fitnessStore.backend.Repository.ProductRepo;
import com.fitnessStore.backend.Repository.UserRepo;
import com.fitnessStore.backend.StorageClasses.CartItemClass;
import com.fitnessStore.backend.StorageClasses.CartItemResponse;
import com.fitnessStore.backend.apiServices.GetUserByToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class CartServicesTest {
    @Mock
    private CartRepo cartRepo;

    @Mock
    private GetUserByToken userByToken;

    @Mock
    private ProductRepo productRepo;

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private CartServices cartServices;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddProduct_Success() {
        UserEntity user = UserEntity.builder().role("user").build();
        ProductEntity product = ProductEntity.builder().id("productId").title("Sample Product").build();
        CartEntity cart = CartEntity.builder().user(user).products(new ArrayList<>()).build();

        when(userByToken.userDetails(anyString())).thenReturn(user);
        when(cartRepo.findByUser(any(UserEntity.class))).thenReturn(cart);
        when(productRepo.findById(anyString())).thenReturn(Optional.of(product));

        ResponseEntity<?> response = cartServices.addProduct("productId", "2", "token");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, ((Map<String, Object>) response.getBody()).get("success"));
        assertEquals(cart, ((Map<String, Object>) response.getBody()).get("data"));
    }

    @Test
    void testAddProduct_InvalidData() {
        UserEntity user = UserEntity.builder().role("user").build();
        when(userByToken.userDetails(anyString())).thenReturn(user);

        ResponseEntity<?> response = cartServices.addProduct("", "2", "token");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(false, ((Map<String, Object>) response.getBody()).get("success"));
        assertEquals("Invalid data provided!", ((Map<String, Object>) response.getBody()).get("message"));
    }

    @Test
    void testAddProduct_ProductNotFound() {
        UserEntity user = UserEntity.builder().role("user").build();
        CartEntity cart = CartEntity.builder().user(user).products(new ArrayList<>()).build();

        when(userByToken.userDetails(anyString())).thenReturn(user);
        when(cartRepo.findByUser(any(UserEntity.class))).thenReturn(cart);
        when(productRepo.findById(anyString())).thenReturn(Optional.empty());

        ResponseEntity<?> response = cartServices.addProduct("productId", "2", "token");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(false, ((Map<String, Object>) response.getBody()).get("success"));
        assertEquals("Product not found", ((Map<String, Object>) response.getBody()).get("message"));
    }

    @Test
    void testGetProducts_Success() {
        UserEntity user = mock(UserEntity.class); // Mock UserEntity
        CartEntity cart = CartEntity.builder().user(user).products(new ArrayList<>()).build();
        CartItemResponse response = CartItemResponse.builder().items(cart.getProducts()).build();

        when(userByToken.userDetails(anyString())).thenReturn(user);
        when(cartRepo.findByUser(any(UserEntity.class))).thenReturn(cart);
        when(user.getCart()).thenReturn(cart);

        ResponseEntity<?> result = cartServices.getProducts("token");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) result.getBody();
        assertTrue((Boolean) responseBody.get("success"));
        assertEquals(response, responseBody.get("data"));
    }

    @Test
    void testGetProducts_CartNotFound() {
        UserEntity user = UserEntity.builder().role("user").build();
        when(userByToken.userDetails(anyString())).thenReturn(user);
        when(cartRepo.findByUser(any(UserEntity.class))).thenReturn(null);

        ResponseEntity<?> response = cartServices.getProducts("token");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(false, ((Map<String, Object>) response.getBody()).get("success"));
        assertEquals("Cart not found", ((Map<String, Object>) response.getBody()).get("message"));
    }

    @Test
    void testDeleteProduct_Success() {
        UserEntity user = UserEntity.builder().role("user").build();
        ProductEntity product = ProductEntity.builder().id("productId").title("Sample Product").build();
        CartEntity cart = CartEntity.builder().user(user).products(new ArrayList<>()).build();
        CartItemResponse response = CartItemResponse.builder().items(cart.getProducts()).build();

        when(userByToken.userDetails(anyString())).thenReturn(user);
        when(productRepo.findById(anyString())).thenReturn(Optional.of(product));
        when(cartRepo.findByUser(any(UserEntity.class))).thenReturn(cart);
        user.setCart(cart);

        ResponseEntity<?> result = cartServices.deleteProduct("productId", "token");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) result.getBody();
        assertTrue((Boolean) responseBody.get("success"));
        assertEquals(response, responseBody.get("data"));
    }

    @Test
    void testDeleteProduct_ProductNotFound() {
        UserEntity user = UserEntity.builder().role("user").build();
        CartEntity cart = CartEntity.builder().user(user).products(new ArrayList<>()).build();

        when(userByToken.userDetails(anyString())).thenReturn(user);
        when(productRepo.findById(anyString())).thenReturn(Optional.empty());
        when(cartRepo.findByUser(any(UserEntity.class))).thenReturn(cart);

        ResponseEntity<?> response = cartServices.deleteProduct("productId", "token");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(false, ((Map<String, Object>) response.getBody()).get("success"));
        assertEquals("Invalid data provided!", ((Map<String, Object>) response.getBody()).get("message"));
    }

    @Test
    void testUpdateProduct_Success() {
        UserEntity user = UserEntity.builder().role("user").build();
        ProductEntity product = ProductEntity.builder().id("productId").title("Sample Product").build();
        CartEntity cart = CartEntity.builder().user(user).products(new ArrayList<>()).build();
        CartItemClass cartItem = CartItemClass.builder().product(product).quantity(1).build();
        cart.getProducts().add(cartItem);
        CartItemResponse response = CartItemResponse.builder().items(cart.getProducts()).build();

        when(userByToken.userDetails(anyString())).thenReturn(user);
        when(productRepo.findById(anyString())).thenReturn(Optional.of(product));
        when(cartRepo.findByUser(any(UserEntity.class))).thenReturn(cart); // Ensure cart is returned
        user.setCart(cart); // Ensure user.getCart() returns the cart

        ResponseEntity<?> result = cartServices.updateProduct("productId", "2", "token");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) result.getBody();
        assertTrue((Boolean) responseBody.get("success"));
        assertEquals(response, responseBody.get("data"));
    }

    @Test
    void testUpdateProduct_ProductNotFound() {
        UserEntity user = UserEntity.builder().role("user").build();
        CartEntity cart = CartEntity.builder().user(user).products(new ArrayList<>()).build();

        when(userByToken.userDetails(anyString())).thenReturn(user);
        when(productRepo.findById(anyString())).thenReturn(Optional.empty());
        when(cartRepo.findByUser(any(UserEntity.class))).thenReturn(cart);

        ResponseEntity<?> response = cartServices.updateProduct("productId", "2", "token");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(false, ((Map<String, Object>) response.getBody()).get("success"));
        assertEquals("Invalid data provided!", ((Map<String, Object>) response.getBody()).get("message"));
    }
}