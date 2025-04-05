package com.fitnessStore.backend.Controller;

import com.fitnessStore.backend.Entity.ReviewEntity;
import com.fitnessStore.backend.Services.ReviewServices;
import jakarta.servlet.http.HttpServletRequest;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/review")
public class ReviewController {

    @Autowired
    private ReviewServices reviewServices;

    @PostMapping("/add")
    public ResponseEntity<?> createReview(@CookieValue(value = "token",defaultValue = "")String token, @RequestBody Map<String,String> credentials){
        return reviewServices.createReview(credentials.get("productId"),credentials.get("reviewMessage"),credentials.get("reviewValue"),token);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getReview(@CookieValue(value = "token",defaultValue = "")String token,@PathVariable String id){
        return reviewServices.getReviewById(id,token);
    }
}
