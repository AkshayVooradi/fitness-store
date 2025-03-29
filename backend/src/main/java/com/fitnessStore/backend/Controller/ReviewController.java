package com.fitnessStore.backend.Controller;

import com.fitnessStore.backend.Entity.ReviewEntity;
import com.fitnessStore.backend.Services.ReviewServices;
import jakarta.servlet.http.HttpServletRequest;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/review")
public class ReviewController {

    @Autowired
    private ReviewServices reviewServices;

    @PostMapping("/add")
    public ResponseEntity<?> createReview(@RequestParam String title, @RequestBody ReviewEntity review, HttpServletRequest request){
        return reviewServices.createReview(title,review,request.getHeader("Authorization"));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateReview(@RequestParam ObjectId id,@RequestBody ReviewEntity review, HttpServletRequest request){
        return reviewServices.updateReview(id,review,request.getHeader("Authorization"));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteReview(@RequestParam ObjectId id,HttpServletRequest request){
        return reviewServices.deleteReview(id,request.getHeader("Authorization"));
    }
}
