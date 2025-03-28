package com.fitnessStore.backend.Controller;

import com.fitnessStore.backend.Services.OrderServices;
import com.fitnessStore.backend.StorageClasses.AddressClass;
import jakarta.servlet.http.HttpServletRequest;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderServices orderServices;

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody AddressClass address, HttpServletRequest request){
        return orderServices.createOrder(address,request.getHeader("Authorization"));
    }

    @DeleteMapping
    private ResponseEntity<?> cancelOrder(@RequestParam String id,@RequestParam String title, HttpServletRequest request){
        return orderServices.cancelOrder(id,title,request.getHeader("Authorization"));
    }
}
