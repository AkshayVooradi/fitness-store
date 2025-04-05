package com.fitnessStore.backend.apiServices;

import com.fitnessStore.backend.Entity.ProductEntity;
import com.fitnessStore.backend.Repository.ProductRepo;
import com.fitnessStore.backend.Repository.ProductRepoCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PublicEPServices {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ProductRepoCustom productRepoCustom;

    public ResponseEntity<?> getProducts(List<String> category,List<String> brand,String sortBy) {

        List<ProductEntity> products = productRepoCustom.filterProducts(category,brand,sortBy);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("success",true);
        responseBody.put("data",products);

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    public ResponseEntity<?> getProductByID(String id) {
        Optional<ProductEntity> product = productRepo.findById(id);

        Map<String,Object> responseBody = new HashMap<>();

        if(product.isEmpty()){

            responseBody.put("success",false);
            responseBody.put("message","Product not found!");

            return new ResponseEntity<>(responseBody,HttpStatus.OK);
        }

        responseBody.put("success",true);
        responseBody.put("data",product);

        return new ResponseEntity<>(responseBody,HttpStatus.OK);
    }

    public ResponseEntity<?> search(String keyword){
        Map<String,Object> responseBody = new HashMap<>();

        if(keyword.isEmpty()){

            responseBody.put("success",false);
            responseBody.put("message","Keyword is required and must be in string format");

            return new ResponseEntity<>(responseBody,HttpStatus.BAD_REQUEST);
        }

        List<ProductEntity> products = productRepoCustom.searchProducts(keyword);

        responseBody.put("success",true);
        responseBody.put("data",products);

        return new ResponseEntity<>(responseBody,HttpStatus.OK);
    }

}