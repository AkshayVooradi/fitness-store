package com.fitnessStore.backend.apiServices;

import com.fitnessStore.backend.Repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PublicEPServices {

    @Autowired
    private ProductRepo productRepo;

    public ResponseEntity<?> getProducts(String category) {
        return new ResponseEntity<>(productRepo.findByCategory(category), HttpStatus.OK);
    }

    public ResponseEntity<?> getProductByTitle(String title) {
        return new ResponseEntity<>(productRepo.findByTitle(title),HttpStatus.OK);
    }
}