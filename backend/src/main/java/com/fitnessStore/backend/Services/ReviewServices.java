package com.fitnessStore.backend.Services;

import com.fitnessStore.backend.Entity.OrderEntity;
import com.fitnessStore.backend.Entity.ProductEntity;
import com.fitnessStore.backend.Entity.ReviewEntity;
import com.fitnessStore.backend.Entity.UserEntity;
import com.fitnessStore.backend.ExceptionHandling.InputArgumentException;
import com.fitnessStore.backend.Repository.OrderRepo;
import com.fitnessStore.backend.Repository.ProductRepo;
import com.fitnessStore.backend.Repository.ReviewRepo;
import com.fitnessStore.backend.Repository.UserRepo;
import com.fitnessStore.backend.StorageClasses.CartItemClass;
import com.fitnessStore.backend.apiServices.GetUserByToken;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ReviewServices {

    @Autowired
    private ReviewRepo reviewRepo;

    @Autowired
    private GetUserByToken getUserByToken;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private OrderRepo orderRepo;

    public ResponseEntity<?> createReview(String productId,String reviewMessage,String reviewValue,String authorization) {
        UserEntity user = getUserByToken.userDetails(authorization);

        Map<String,Object> responseBody = new HashMap<>();

        if(user == null){
            responseBody.put("success",false);
            responseBody.put("message","No user found");
            return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
        }

        List<OrderEntity> orders = orderRepo.findByUserId(user.getId());

        boolean ordered = false;

        for(OrderEntity order: orders){
            if(order.getOrderStatus().equals("delivered")) {
                for (CartItemClass item : order.getCartItems()) {
                    if (item.getProduct().getId().equals(productId)) {
                        ordered = true;
                        break;
                    }
                }
            }
        }

        if(!ordered){

            responseBody.put("success",false);
            responseBody.put("message","You need to purchase product to review it");

            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        }

        List<ReviewEntity> reviews = reviewRepo.findByProductId(productId);

        ReviewEntity review = null;

        for(ReviewEntity rev : reviews){
            if(rev.getUserId().equals(user.getId())){
                review = rev;
                break;
            }
        }

        Optional<ProductEntity> product = productRepo.findById(productId);

        if(product.isEmpty()){

            responseBody.put("success",false);
            responseBody.put("message","product not found");

            return new ResponseEntity<>(responseBody, HttpStatus.FORBIDDEN);
        }

        if(review == null){

            review = ReviewEntity.builder()
                    .productId(productId)
                    .user(user)
                    .product(product.get())
                    .userId(user.getId())
                    .userName(user.getUsername())
                    .reviewMessage(reviewMessage)
                    .reviewValue(Integer.parseInt(reviewValue))
                    .build();

            product.get().setSumOfRatings(product.get().getSumOfRatings()+Integer.parseInt(reviewValue));
            product.get().setAverageReview((float)product.get().getSumOfRatings()/reviews.size());

            reviewRepo.save(review);
            productRepo.save(product.get());

        }else{

            product.get().setSumOfRatings(product.get().getSumOfRatings()+Integer.parseInt(reviewValue)-review.getReviewValue());
            product.get().setAverageReview((float)product.get().getSumOfRatings()/reviews.size());

            review.setReviewMessage(reviewMessage);
            review.setReviewValue(Integer.parseInt(reviewValue));

            reviewRepo.save(review);
            productRepo.save(product.get());

        }

        responseBody.put("success",true);
        responseBody.put("data",review);
        responseBody.put("message","Added review");

        return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
    }

    public ResponseEntity<?> getReviewById(String id, String token) {
        UserEntity user = getUserByToken.userDetails(token);

        Map<String,Object> responseBody = new HashMap<>();

        if(user == null){

            responseBody.put("success",false);
            responseBody.put("message","No user found");

            return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);

        }

        List<ReviewEntity> reviews = reviewRepo.findByProductId(id);

        responseBody.put("success",true);
        responseBody.put("data",reviews);

        return new ResponseEntity<>(responseBody, HttpStatus.OK);

    }
}
