package com.fitnessStore.backend.Services;

import com.fitnessStore.backend.Entity.CartEntity;
import com.fitnessStore.backend.Entity.OrderEntity;
import com.fitnessStore.backend.Entity.ProductEntity;
import com.fitnessStore.backend.Entity.UserEntity;
import com.fitnessStore.backend.Repository.CartRepo;
import com.fitnessStore.backend.Repository.OrderRepo;
import com.fitnessStore.backend.Repository.ProductRepo;
import com.fitnessStore.backend.Repository.UserRepo;
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
class OrderServicesTest {
    @InjectMocks
    private OrderServices orderServices;

    @Mock
    private GetUserByToken getUserByToken;

    @Mock
    private UserRepo userRepo;

    @Mock
    private OrderRepo orderRepo;

    @Mock
    private ProductRepo productRepo;

    @Mock
    private CartRepo cartRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCartEmpty() {
        String totalCartAmount = "100.0";
        String authorization = "authToken";
        UserEntity user = new UserEntity();
        CartEntity cart = new CartEntity();
        cart.setProducts(new ArrayList<>());
        user.setCart(cart);
        when(getUserByToken.userDetails(authorization)).thenReturn(user);

        ResponseEntity<?> response = orderServices.createOrder(totalCartAmount, authorization);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertFalse((Boolean) ((Map<String, Object>) response.getBody()).get("success"));
        assertEquals("Cart is Empty", ((Map<String, Object>) response.getBody()).get("message"));
    }

    @Test
    void testCreateOrder_InvalidQuantity() {
        String totalCartAmount = "100.0";
        String authorization = "authToken";
        UserEntity user = new UserEntity();
        CartEntity cart = new CartEntity();
        CartItemClass item = new CartItemClass();
        ProductEntity product = new ProductEntity();
        product.setTotalStock(1);
        item.setProduct(product);
        item.setQuantity(2);
        cart.setProducts(Arrays.asList(item));
        user.setCart(cart);
        when(getUserByToken.userDetails(authorization)).thenReturn(user);

        ResponseEntity<?> response = orderServices.createOrder(totalCartAmount, authorization);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse((Boolean) ((Map<String, Object>) response.getBody()).get("success"));
        assertEquals("Invalid quantity selected", ((Map<String, Object>) response.getBody()).get("message"));
    }

    @Test
    void testCreateOrder_Success() {
        String totalCartAmount = "100.0";
        String authorization = "authToken";
        UserEntity user = new UserEntity();
        CartEntity cart = new CartEntity();
        CartItemClass item = new CartItemClass();
        ProductEntity product = new ProductEntity();
        product.setTotalStock(10);
        item.setProduct(product);
        item.setQuantity(1);
        cart.setProducts(Arrays.asList(item));
        user.setCart(cart);
        when(getUserByToken.userDetails(authorization)).thenReturn(user);

        ResponseEntity<?> response = orderServices.createOrder(totalCartAmount, authorization);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue((Boolean) ((Map<String, Object>) response.getBody()).get("success"));
        assertEquals("order created successfully", ((Map<String, Object>) response.getBody()).get("message"));
    }

    @Test
    void testGetOrders_NoOrdersFound() {
        String authorization = "authToken";
        UserEntity user = new UserEntity();
        user.setOrders(new ArrayList<>());
        when(getUserByToken.userDetails(authorization)).thenReturn(user);

        ResponseEntity<?> response = orderServices.getOrders(authorization);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertFalse((Boolean) ((Map<String, Object>) response.getBody()).get("success"));
        assertEquals("no orders found", ((Map<String, Object>) response.getBody()).get("message"));
    }

//    @Test
//    void testGetOrders_Success() {
//        String authorization = "authToken";
//        UserEntity user = new UserEntity();
//        OrderEntity order = new OrderEntity();
//        user.setOrders(Arrays.asList(order));
//        when(getUserByToken.userDetails(authorization)).thenReturn(user);
//
//        ResponseEntity<?> response = orderServices.getOrders(authorization);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertTrue((Boolean) ((Map<String, Object>) response.getBody()).get("success"));
//        assertEquals(Arrays.asList(order), ((Map<String, Object>) response.getBody()).get("data"));
//    }

    @Test
    void testGetOrderById_OrderNotFound() {
        String id = "123";
        when(orderRepo.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<?> response = orderServices.getOrderById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertFalse((Boolean) ((Map<String, Object>) response.getBody()).get("success"));
        assertEquals("order not found", ((Map<String, Object>) response.getBody()).get("message"));
    }

    @Test
    void testGetOrderById_Success() {
        String id = "123";
        OrderEntity order = new OrderEntity();
        when(orderRepo.findById(id)).thenReturn(Optional.of(order));

        ResponseEntity<?> response = orderServices.getOrderById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) ((Map<String, Object>) response.getBody()).get("success"));
        assertEquals(Optional.of(order), ((Map<String, Object>) response.getBody()).get("data"));
    }

    @Test
    void testCancelOrder_NoOrderFound() {
        String id = "123";
        String authorization = "authToken";
        UserEntity user = new UserEntity();
        user.setOrders(new ArrayList<>());
        when(getUserByToken.userDetails(authorization)).thenReturn(user);

        ResponseEntity<?> response = orderServices.cancelOrder(id, authorization);

        Map<String,Object> responseBody= new HashMap<>();
        responseBody.put("success",false);
        responseBody.put("message","order not found");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(responseBody, response.getBody());
    }

    @Test
    void testCancelOrder_EmptyCart() {
        String id = "123";
        String authorization = "authToken";
        UserEntity user = new UserEntity();
        OrderEntity order = new OrderEntity();
        user.setOrders(Arrays.asList(order));
        when(getUserByToken.userDetails(authorization)).thenReturn(user);
        when(orderRepo.findById(id)).thenReturn(Optional.of(order));

        ResponseEntity<?> response = orderServices.cancelOrder(id, authorization);

        Map<String,Object> responseBody= new HashMap<>();
        responseBody.put("success",false);
        responseBody.put("message","Cart is empty");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(responseBody, response.getBody());
    }

    @Test
    void testCancelOrder_Success() {
        String id = new ObjectId().toHexString();
        String title = "ProductTitle";
        String prodId = new ObjectId().toHexString();
        String authorization = "authToken";
        UserEntity user = new UserEntity();
        OrderEntity order = new OrderEntity();
        order.setId(id);

        ProductEntity product = new ProductEntity();
        product.setTitle(title);
        product.setId(prodId);

        CartItemClass item = new CartItemClass();
        item.setProduct(product);
        item.setProductId(prodId);

        order.setCartItems(Arrays.asList(item));
        user.setOrders(Arrays.asList(order));

        when(getUserByToken.userDetails(authorization)).thenReturn(user);
        when(productRepo.findById(prodId)).thenReturn(Optional.of(product));
        when(orderRepo.findById(id)).thenReturn(Optional.of(order));

        ResponseEntity<?> response = orderServices.cancelOrder(id, authorization);

        Map<String,Object> responseBody = new HashMap<>();

        responseBody.put("success",true);
        responseBody.put("message","Cancelled the Order");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseBody, response.getBody());
    }

}