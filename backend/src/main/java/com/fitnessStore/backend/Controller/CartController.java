package com.fitnessStore.backend.Controller;

import com.fitnessStore.backend.Entity.ProductEntity;
import com.fitnessStore.backend.Services.CartServices;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartServices cartServices;

    @PostMapping("/add")
    public ResponseEntity<?> AddProduct(@RequestParam String title,@RequestParam int quantity, HttpServletRequest request){
        return cartServices.addProduct(title,quantity,request.getHeader("Authorization"));
    }

    @GetMapping
    public ResponseEntity<?> getProducts(HttpServletRequest request){
        return cartServices.getProducts(request.getHeader("Authorization"));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteProduct(@RequestParam String title,HttpServletRequest request){
        return cartServices.deleteProduct(title, request.getHeader("Authorization"));
    }

    @PutMapping("/update")
    private ResponseEntity<?> updateProduct(@RequestParam String title,@RequestParam int quantity, HttpServletRequest request){
        return cartServices.updateProduct(title,quantity,request.getHeader("Authorization"));
    }
}
