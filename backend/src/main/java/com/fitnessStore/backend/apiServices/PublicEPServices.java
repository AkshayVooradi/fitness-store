package com.fitnessStore.backend.apiServices;

import com.fitnessStore.backend.Repository.ProductRepo;
import com.fitnessStore.backend.Repository.ProductRepoCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PublicEPServices {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ProductRepoCustom productRepoCustom;

    public ResponseEntity<?> getProducts(String category) {
        return new ResponseEntity<>(productRepo.findByCategory(category), HttpStatus.OK);
    }

    public ResponseEntity<?> getProductByTitle(String title) {
        return new ResponseEntity<>(productRepo.findByTitle(title),HttpStatus.OK);
    }

    public ResponseEntity<?> search(String keyword){
        return new ResponseEntity<>(productRepoCustom.searchProducts(keyword),HttpStatus.OK);
    }

    public ResponseEntity<?> filter(String category, Double minPrice, Double maxPrice, Integer minDiscount, Double minRating) {
        return new ResponseEntity<>(productRepoCustom.filterProducts(category,minPrice,maxPrice,minDiscount,minRating),HttpStatus.OK);
    }
}