package com.fitnessStore.backend.Controller;

import com.fitnessStore.backend.Services.OrderServices;
import com.fitnessStore.backend.StorageClasses.AddressClass;
import jakarta.servlet.http.HttpServletRequest;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderServices orderServices;

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@CookieValue(value = "token",defaultValue = "")String token, @RequestBody Map<String,String> credentials){
        return orderServices.createOrder(credentials.get("totalCartAmount"),token);
    }

    @GetMapping("/list")
    public ResponseEntity<?> getOrders(@CookieValue(value = "token",defaultValue = "")String token){
        return orderServices.getOrders(token);
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable String id){
        return orderServices.getOrderById(id);
    }

    @PutMapping("/cancel/{id}")
    ResponseEntity<?> cancelOrder(@CookieValue(value = "token",defaultValue = "") String token,@PathVariable String id){
        return orderServices.cancelOrder(id,token);
    }
}
