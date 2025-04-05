package com.fitnessStore.backend.Controller;

import com.fitnessStore.backend.Entity.ProductEntity;
import com.fitnessStore.backend.Services.AdminServices;
import jakarta.servlet.http.HttpServletRequest;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminServices adminServices;

    @PostMapping("/product/upload-image")
    public ResponseEntity<?> uploadImage(@RequestParam("my_file") MultipartFile file,@CookieValue(value = "token",defaultValue = "")String token){
        return adminServices.uploadImage(file,token);
    }


    @PostMapping("/product/add")
    public ResponseEntity<?> AddProducts(@RequestBody Map<String,String> credentials, @CookieValue(value = "token",defaultValue = "")String token){
        return adminServices.addProduct(credentials.get("title"),credentials.get("category"),credentials.get("brand"),credentials.get("price"),credentials.get("salePrice"),credentials.get("description"),credentials.get("totalStock"),token);
    }

    @DeleteMapping("/product/delete/{id}")
    public ResponseEntity<?> DeleteProducts(@CookieValue(value = "token",defaultValue = "")String token, @PathVariable String id){
        return adminServices.deleteProduct(id,token);
    }

    @GetMapping("/product/getAllProducts")
    public ResponseEntity<?> RetrieveProducts(@CookieValue(value = "token",defaultValue = "")String token){
        return adminServices.getAllProducts(token);
    }

    @PostMapping("/product/updateProduct/{id}")
    public ResponseEntity<?> UpdateProduct(@CookieValue(value = "token",defaultValue = "")String token,@PathVariable String id,@RequestBody Map<String,String> credentials){
        return adminServices.updateProduct(id,credentials.get("title"),credentials.get("category"),credentials.get("brand"),credentials.get("price"),credentials.get("salePrice"),credentials.get("description"),credentials.get("totalStock"),token);
    }

    @GetMapping("/orders/get")
    public ResponseEntity<?> getAllOrders(@CookieValue(value = "token",defaultValue = "")String token){
        return adminServices.getAllOrders(token);
    }

    @GetMapping("/orders/details/{id}")
    public ResponseEntity<?> getOrderById(@CookieValue(value = "token",defaultValue = "")String token,@PathVariable String id){
        return adminServices.getOrderById(id,token);
    }

    @PutMapping("/orders/update/{id}")
    public ResponseEntity<?> updateOrderById(@CookieValue(value = "token",defaultValue = "")String token,@PathVariable String id,@RequestBody Map<String,String> credentials){
        return adminServices.updateOrderById(id,token,credentials.get("orderStatus"));
    }
}
