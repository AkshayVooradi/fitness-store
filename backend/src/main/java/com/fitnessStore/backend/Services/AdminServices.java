package com.fitnessStore.backend.Services;

import com.fitnessStore.backend.Entity.ProductEntity;
import com.fitnessStore.backend.Entity.UserEntity;
import com.fitnessStore.backend.Repository.ProductRepo;
import com.fitnessStore.backend.apiServices.GetUserByToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AdminServices {


    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private GetUserByToken userByToken;


    public ResponseEntity<?> addProduct(ProductEntity product, String authHeader) {
        UserEntity user = userByToken.userDetails(authHeader);

        if(!user.getRole().equals("ADMIN")){
            return new ResponseEntity<>("Unauthorized user",HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(productRepo.save(product),HttpStatus.CREATED);
    }

    public ResponseEntity<?> deleteProduct(String productTitle, String authHeader) {
        UserEntity user = userByToken.userDetails(authHeader);

        if(!user.getRole().equals("ADMIN")){
            return new ResponseEntity<>("Unauthorized user",HttpStatus.UNAUTHORIZED);
        }

        ProductEntity product = productRepo.findByTitle(productTitle);

        if(product == null){
            return new ResponseEntity<>("Product Not Found",HttpStatus.NOT_FOUND);
        }

        productRepo.delete(product);

        return new ResponseEntity<>("Deleted Successfully",HttpStatus.NO_CONTENT);

    }

    public ResponseEntity<?> getAllProducts(String category, String authHeader) {
        UserEntity user = userByToken.userDetails(authHeader);

        if(!user.getRole().equals("ADMIN")){
            return new ResponseEntity<>("Unauthorized user",HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(productRepo.findByCategory(category),HttpStatus.OK);
    }

    public ResponseEntity<?> updateProduct(String productTitle, ProductEntity newProduct, String authHeader) {
        UserEntity user = userByToken.userDetails(authHeader);

        if(!user.getRole().equals("ADMIN")){
            return new ResponseEntity<>("Unauthorized user",HttpStatus.UNAUTHORIZED);
        }

        ProductEntity product = productRepo.findByTitle(productTitle);

        if(product == null){
            return new ResponseEntity<>("Product Not Found",HttpStatus.NOT_FOUND);
        }

        if(newProduct.getPrice() != 0){
            product.setPrice(newProduct.getPrice());
        }

        if(newProduct.getDiscountPercent()!=0){
            product.setDiscountPercent(newProduct.getDiscountPercent());
        }

        if(newProduct.getStock()!=0){
            product.setStock(newProduct.getStock());
        }

        productRepo.save(product);

        return new ResponseEntity<>(product,HttpStatus.OK);
    }

}
