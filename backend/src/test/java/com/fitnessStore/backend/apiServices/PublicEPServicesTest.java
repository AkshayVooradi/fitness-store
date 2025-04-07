package com.fitnessStore.backend.apiServices;

import com.fitnessStore.backend.Entity.ProductEntity;
import com.fitnessStore.backend.Entity.UserEntity;
import com.fitnessStore.backend.Repository.ProductRepo;
import com.fitnessStore.backend.Repository.ProductRepoCustom;
import com.fitnessStore.backend.Repository.UserRepo;
import com.fitnessStore.backend.StorageClasses.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class PublicEPServicesTest {
    @InjectMocks
    private PublicEPServices publicEPServices;

    @Mock
    ProductRepo productRepo;

    @Mock
    ProductRepoCustom productRepoCustom;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetProducts() {
       List<String> category = Arrays.asList("Fitness", "Health");
        List<String> brand = Arrays.asList("BrandA", "BrandB");
        String sortBy = "price";
        List<ProductEntity> products = new ArrayList<>();
        when(productRepoCustom.filterProducts(category, brand, sortBy)).thenReturn(products);

        ResponseEntity<?> response = publicEPServices.getProducts(category, brand, sortBy);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) ((Map<String, Object>) response.getBody()).get("success"));
        assertEquals(products, ((Map<String, Object>) response.getBody()).get("data"));
    }

    @Test
    void testGetProductByID_ProductFound() {
        String id = "123";
        ProductEntity productEntity = new ProductEntity();
        when(productRepo.findById(id)).thenReturn(Optional.of(productEntity));

        ResponseEntity<?> response = publicEPServices.getProductByID(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) ((Map<String, Object>) response.getBody()).get("success"));
        assertEquals(Optional.of(productEntity), ((Map<String, Object>) response.getBody()).get("data"));
    }

    @Test
    void testGetProductByID_ProductNotFound() {
        String id = "123";
        when(productRepo.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<?> response = publicEPServices.getProductByID(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse((Boolean) ((Map<String, Object>) response.getBody()).get("success"));
        assertEquals("Product not found!", ((Map<String, Object>) response.getBody()).get("message"));
    }

    @Test
    void testSearch_KeywordProvided() {
        String keyword = "fitness";
        List<ProductEntity> products = new ArrayList<>();
        when(productRepoCustom.searchProducts(keyword)).thenReturn(products);

        ResponseEntity<?> response = publicEPServices.search(keyword);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) ((Map<String, Object>) response.getBody()).get("success"));
        assertEquals(products, ((Map<String, Object>) response.getBody()).get("data"));
    }

    @Test
    void testSearch_KeywordNotProvided() {
        String keyword = "";

        ResponseEntity<?> response = publicEPServices.search(keyword);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse((Boolean) ((Map<String, Object>) response.getBody()).get("success"));
        assertEquals("Keyword is required and must be in string format", ((Map<String, Object>) response.getBody()).get("message"));
    }
}