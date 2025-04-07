package com.fitnessStore.backend.Controller;

import com.fitnessStore.backend.Services.CartServices;
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
class CartControllerTest {
    @Mock
    private CartServices cartServices;

    @InjectMocks
    private CartController cartController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddProduct() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("productId", "123");
        credentials.put("quantity", "2");

        when(cartServices.addProduct(anyString(), anyString(), anyString()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> result = cartController.AddProduct("token", credentials);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(cartServices, times(1)).addProduct("123", "2", "token");
    }

    @Test
    public void testGetProducts() {
        when(cartServices.getProducts(anyString()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> result = cartController.getProducts("token");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(cartServices, times(1)).getProducts("token");
    }

    @Test
    public void testDeleteProduct() {
        when(cartServices.deleteProduct(anyString(), anyString()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> result = cartController.deleteProduct("token", "123");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(cartServices, times(1)).deleteProduct("123", "token");
    }

    @Test
    public void testUpdateProduct() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("productId", "123");
        credentials.put("quantity", "3");

        when(cartServices.updateProduct(anyString(), anyString(), anyString()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> result = cartController.updateProduct("token", credentials);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(cartServices, times(1)).updateProduct("123", "3", "token");
    }
}