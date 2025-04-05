package com.fitnessStore.backend.Controller;

import com.fitnessStore.backend.Entity.ProductEntity;
import com.fitnessStore.backend.Services.CartServices;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartServices cartServices;

    @PostMapping("/add")
    public ResponseEntity<?> AddProduct(@CookieValue(value = "token",defaultValue = "")String token,@RequestBody Map<String ,String> credentials){
        return cartServices.addProduct(credentials.get("productId"),credentials.get("quantity"),token);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getProducts(@CookieValue(value = "token",defaultValue = "")String token){
        return cartServices.getProducts(token);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@CookieValue(value = "token",defaultValue = "")String token,@PathVariable String id){
        return cartServices.deleteProduct(id,token);
    }

    @PutMapping("/update")
    private ResponseEntity<?> updateProduct(@CookieValue(value = "token", defaultValue = "")String token,@RequestBody Map<String ,String> credentials){
        return cartServices.updateProduct(credentials.get("productId"),credentials.get("quantity"),token);
    }
}
