package com.fitnessStore.backend.Controller;

import com.fitnessStore.backend.Services.AdminServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
class AdminControllerTest {
    @Mock
    private AdminServices adminServices;

    @InjectMocks
    private AdminController adminController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUploadImage() {
        MultipartFile file = mock(MultipartFile.class);

        when(adminServices.uploadImage(any(MultipartFile.class), anyString()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> result = adminController.uploadImage(file, "token");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(adminServices, times(1)).uploadImage(file, "token");
    }

    @Test
    public void testAddProducts() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("title", "Product Title");
        credentials.put("category", "Category");
        credentials.put("brand", "Brand");
        credentials.put("price", "100");
        credentials.put("salePrice", "80");
        credentials.put("description", "Product Description");
        credentials.put("image","img_url");
        credentials.put("totalStock", "50");

        when(adminServices.addProduct(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> result = adminController.AddProducts(credentials, "token");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(adminServices, times(1)).addProduct("Product Title", "Category", "Brand", "100", "80", "Product Description", "50", "img_url","token");
    }

    @Test
    public void testDeleteProducts() {
        when(adminServices.deleteProduct(anyString(), anyString()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> result = adminController.DeleteProducts("token", "123");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(adminServices, times(1)).deleteProduct("123", "token");
    }

    @Test
    public void testRetrieveProducts() {
        when(adminServices.getAllProducts(anyString()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> result = adminController.RetrieveProducts("token");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(adminServices, times(1)).getAllProducts("token");
    }

    @Test
    public void testUpdateProduct() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("title", "Updated Title");
        credentials.put("category", "Updated Category");
        credentials.put("brand", "Updated Brand");
        credentials.put("price", "120");
        credentials.put("salePrice", "100");
        credentials.put("description", "Updated Description");
        credentials.put("totalStock", "60");

        when(adminServices.updateProduct(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),anyString()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> result = adminController.UpdateProduct("token", "123", credentials);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(adminServices, times(1)).updateProduct("123", "Updated Title", "Updated Category", "Updated Brand", "120", "100", "Updated Description", "60", "token");
    }

    @Test
    public void testGetAllOrders() {
        when(adminServices.getAllOrders(anyString()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> result = adminController.getAllOrders("token");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(adminServices, times(1)).getAllOrders("token");
    }

    @Test
    public void testGetOrderById() {
        when(adminServices.getOrderById(anyString(), anyString()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> result = adminController.getOrderById("token", "123");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(adminServices, times(1)).getOrderById("123", "token");
    }

    @Test
    public void testUpdateOrderById() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("orderStatus", "Shipped");

        when(adminServices.updateOrderById(anyString(), anyString(), anyString()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> result = adminController.updateOrderById("token", "123", credentials);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(adminServices, times(1)).updateOrderById("123", "token", "Shipped");
    }

}