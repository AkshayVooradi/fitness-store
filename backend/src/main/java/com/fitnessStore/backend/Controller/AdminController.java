package com.fitnessStore.backend.Controller;

import com.fitnessStore.backend.Entity.ProductEntity;
import com.fitnessStore.backend.Services.AdminServices;
import jakarta.servlet.http.HttpServletRequest;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/admin/product")
public class AdminController {

    @Autowired
    private AdminServices adminServices;


    @PostMapping("/add")
    public ResponseEntity<?> AddProducts(@RequestParam("images") List<MultipartFile> files,
                                         @RequestParam("title")String title,
                                         @RequestParam("category")String category,
                                         @RequestParam("brand")String brand,
                                         @RequestParam("price")Double price,
                                         @RequestParam("discountPercent")Integer discountPercent,
                                         @RequestParam("description")String description,
                                         @RequestParam("stock")Integer stock,
                                         HttpServletRequest request){
        return adminServices.addProduct(files,title,category,brand,price,discountPercent,description,stock,request.getHeader("Authorization"));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> DeleteProducts(@RequestParam String title, HttpServletRequest request){
        return adminServices.deleteProduct(title,request.getHeader("Authorization"));
    }

    @GetMapping("/getAllProducts")
    public ResponseEntity<?> RetrieveProducts(@RequestParam String category, HttpServletRequest request){
        return adminServices.getAllProducts(category,request.getHeader("Authorization"));
    }

    @PutMapping("/updateProduct")
    public ResponseEntity<?> UpdateProduct(@RequestParam String title,@RequestBody ProductEntity product, HttpServletRequest request){
        return adminServices.updateProduct(title,product,request.getHeader("Authorization"));
    }

    @PutMapping("/updateOrder")
    public ResponseEntity<?> UpdateOrderStatus(@RequestParam String id, @RequestParam String title, @RequestParam String userName,@RequestParam String status,HttpServletRequest request){
        return adminServices.updateOrder(id,title,userName,status,request.getHeader("Authorization"));
    }
}
