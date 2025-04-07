package com.fitnessStore.backend.Services;

import static org.junit.jupiter.api.Assertions.*;

import com.fitnessStore.backend.Entity.*;
import com.fitnessStore.backend.Repository.*;
import com.fitnessStore.backend.StorageClasses.CartItemClass;
import com.fitnessStore.backend.apiServices.GetUserByToken;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class ReviewServicesTest {
    @InjectMocks
    private ReviewServices reviewServices;

    @Mock
    private ReviewRepo reviewRepo;

    @Mock
    private GetUserByToken getUserByToken;

    @Mock
    private ProductRepo productRepo;

    @Mock
    private UserRepo userRepo;

    @Mock
    private OrderRepo orderRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateReview_UserNotFound() {
        String productId = "123";
        String reviewMessage = "Great product!";
        String reviewValue = "5";
        String authorization = "authToken";
        when(getUserByToken.userDetails(authorization)).thenReturn(null);

        ResponseEntity<?> response = reviewServices.createReview(productId, reviewMessage, reviewValue, authorization);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertFalse((Boolean) ((Map<String, Object>) response.getBody()).get("success"));
        assertEquals("No user found", ((Map<String, Object>) response.getBody()).get("message"));
    }

    @Test
    void testCreateReview_ProductNotPurchased() {
        String productId = "123";
        String reviewMessage = "Great product!";
        String reviewValue = "5";
        String authorization = "authToken";
        UserEntity user = new UserEntity();
        user.setId("userId");
        when(getUserByToken.userDetails(authorization)).thenReturn(user);
        when(orderRepo.findByUserId(user.getId())).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = reviewServices.createReview(productId, reviewMessage, reviewValue, authorization);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse((Boolean) ((Map<String, Object>) response.getBody()).get("success"));
        assertEquals("You need to purchase product to review it", ((Map<String, Object>) response.getBody()).get("message"));
    }

    @Test
    void testCreateReview_ProductNotFound() {
        String productId = "123";
        String reviewMessage = "Great product!";
        String reviewValue = "5";
        String authorization = "authToken";
        UserEntity user = new UserEntity();
        user.setId("userId");
        when(getUserByToken.userDetails(authorization)).thenReturn(user);
        OrderEntity order = new OrderEntity();
        order.setOrderStatus("delivered");
        CartItemClass item = new CartItemClass();
        ProductEntity product = new ProductEntity();
        product.setId(productId);
        item.setProduct(product);
        order.setCartItems(Arrays.asList(item));
        when(orderRepo.findByUserId(user.getId())).thenReturn(Arrays.asList(order));
        when(productRepo.findById(productId)).thenReturn(Optional.empty());

        ResponseEntity<?> response = reviewServices.createReview(productId, reviewMessage, reviewValue, authorization);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertFalse((Boolean) ((Map<String, Object>) response.getBody()).get("success"));
        assertEquals("product not found", ((Map<String, Object>) response.getBody()).get("message"));
    }

    @Test
    void testCreateReview_Success() {
        String productId = "123";
        String reviewMessage = "Great product!";
        String reviewValue = "5";
        String authorization = "authToken";
        UserEntity user = new UserEntity();
        user.setId("userId");
        when(getUserByToken.userDetails(authorization)).thenReturn(user);
        OrderEntity order = new OrderEntity();
        order.setOrderStatus("delivered");
        CartItemClass item = new CartItemClass();
        ProductEntity product = new ProductEntity();
        product.setId(productId);
        item.setProduct(product);
        order.setCartItems(Arrays.asList(item));
        when(orderRepo.findByUserId(user.getId())).thenReturn(Arrays.asList(order));
        when(productRepo.findById(productId)).thenReturn(Optional.of(product));
        when(reviewRepo.findByProductId(productId)).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = reviewServices.createReview(productId, reviewMessage, reviewValue, authorization);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue((Boolean) ((Map<String, Object>) response.getBody()).get("success"));
        assertEquals("Added review", ((Map<String, Object>) response.getBody()).get("message"));
    }

    @Test
    void testGetReviewById_UserNotFound() {
        String id = "123";
        String token = "authToken";
        when(getUserByToken.userDetails(token)).thenReturn(null);

        ResponseEntity<?> response = reviewServices.getReviewById(id, token);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertFalse((Boolean) ((Map<String, Object>) response.getBody()).get("success"));
        assertEquals("No user found", ((Map<String, Object>) response.getBody()).get("message"));
    }

    @Test
    void testGetReviewById_Success() {
        String id = "123";
        String token = "authToken";
        UserEntity user = new UserEntity();
        when(getUserByToken.userDetails(token)).thenReturn(user);
        List<ReviewEntity> reviews = new ArrayList<>();
        when(reviewRepo.findByProductId(id)).thenReturn(reviews);

        ResponseEntity<?> response = reviewServices.getReviewById(id, token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) ((Map<String, Object>) response.getBody()).get("success"));
        assertEquals(reviews, ((Map<String, Object>) response.getBody()).get("data"));
    }
}