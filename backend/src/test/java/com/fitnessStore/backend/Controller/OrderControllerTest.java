package com.fitnessStore.backend.Controller;

import com.fitnessStore.backend.Services.OrderServices;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
class OrderControllerTest {
    @Mock
    private OrderServices orderServices;

    @InjectMocks
    private OrderController orderController;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateOrder() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("totalCartAmount", "100");

        when(orderServices.createOrder(anyString(), anyString()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> result = orderController.createOrder("token", credentials);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(orderServices, times(1)).createOrder("100", "token");
    }

    @Test
    public void testGetOrders() {
        when(orderServices.getOrders(anyString()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> result = orderController.getOrders("token");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(orderServices, times(1)).getOrders("token");
    }

    @Test
    public void testGetOrderById() {
        when(orderServices.getOrderById(anyString()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> result = orderController.getOrderById("123");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(orderServices, times(1)).getOrderById("123");
    }

    @Test
    public void testCancelOrder() {
        when(request.getHeader("Authorization")).thenReturn("Bearer token");
        when(orderServices.cancelOrder(anyString(), anyString(), anyString()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> result = orderController.cancelOrder("123", "title", request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(orderServices, times(1)).cancelOrder("123", "title", "Bearer token");
    }
}