package com.fitnessStore.backend.Controller;

import com.fitnessStore.backend.apiServices.PublicEPServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
class PublicEPControllerTest {
    @Mock
    private PublicEPServices publicEPServices;

    @InjectMocks
    private PublicEPController publicEPController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetProducts() {
        List<String> category = Arrays.asList("electronics", "home");
        List<String> brand = Arrays.asList("brandA", "brandB");
        String sortBy = "price";

        when(publicEPServices.getProducts(anyList(), anyList(), anyString()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> result = publicEPController.getProducts(category, brand, sortBy);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(publicEPServices, times(1)).getProducts(category, brand, sortBy);
    }

    @Test
    public void testGetProductByTitle() {
        when(publicEPServices.getProductByID(anyString()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> result = publicEPController.getProductByTitle("123");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(publicEPServices, times(1)).getProductByID("123");
    }

    @Test
    public void testSearchProduct() {
        when(publicEPServices.search(anyString()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> result = publicEPController.searchProduct("keyword");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(publicEPServices, times(1)).search("keyword");
    }
}