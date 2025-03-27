package com.fitnessStore.backend.Controller;

import com.fitnessStore.backend.Entity.ProductEntity;
import com.fitnessStore.backend.Services.AdminServices;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/product")
public class AdminController {

    @Autowired
    private AdminServices adminServices;


    @PostMapping("/add")
    public ResponseEntity<?> AddProducts(@RequestBody ProductEntity product, HttpServletRequest request){
        return adminServices.addProduct(product,request.getHeader("Authorization"));
    }

    @DeleteMapping("/delete/{productTitle}")
    public ResponseEntity<?> DeleteProducts(@PathVariable String productTitle, HttpServletRequest request){
        return adminServices.deleteProduct(productTitle,request.getHeader("Authorization"));
    }

    @GetMapping("/getAllProducts/{category}")
    public ResponseEntity<?> RetrieveProducts(@PathVariable String category, HttpServletRequest request){
        return adminServices.getAllProducts(category,request.getHeader("Authorization"));
    }

    @PutMapping("/updateProduct/{productTitle}")
    public ResponseEntity<?> UpdateProduct(@PathVariable String productTitle,@RequestBody ProductEntity product, HttpServletRequest request){
        return adminServices.updateProduct(productTitle,product,request.getHeader("Authorization"));
    }
}
