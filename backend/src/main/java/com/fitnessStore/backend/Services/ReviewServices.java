package com.fitnessStore.backend.Services;

import com.fitnessStore.backend.Entity.ProductEntity;
import com.fitnessStore.backend.Entity.ReviewEntity;
import com.fitnessStore.backend.Entity.UserEntity;
import com.fitnessStore.backend.Repository.ProductRepo;
import com.fitnessStore.backend.Repository.ReviewRepo;
import com.fitnessStore.backend.Repository.UserRepo;
import com.fitnessStore.backend.apiServices.GetUserByToken;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public ResponseEntity<?> createReview(String title, ReviewEntity review, String authorization) {
        UserEntity user = getUserByToken.userDetails(authorization);

        if(user == null){
            return new ResponseEntity<>("No user found", HttpStatus.NOT_FOUND);
        }

        ProductEntity product = productRepo.findByTitle(title);

        if(product == null){
            return new ResponseEntity<>("Product Not found",HttpStatus.NOT_FOUND);
        }

        List<ProductEntity> products = user.getProducts();

        boolean found= false;

        for(ProductEntity prod: products){
            if(prod.getTitle().equals(product.getTitle())){
                found= true;
                break;
            }
        }

        if(!found){
            return new ResponseEntity<>("You haven't purchased the product to write a review",HttpStatus.UNAUTHORIZED);
        }

        review.setUser(user);
        review.setCreatedAt(LocalDateTime.now());
        review.setProduct(product);


        List<ReviewEntity> userReviews= user.getReviews();
        if(userReviews == null){
            userReviews = new ArrayList<>();
        }

        userReviews.add(review);


        List<ReviewEntity> productReviews = product.getReviews();

        if(productReviews == null){
            productReviews = new ArrayList<>();
        }

        productReviews.add(review);
        product.setSumOfRatings(product.getSumOfRatings()+review.getRating());
        product.setAverageRating((float)product.getSumOfRatings()/productReviews.size());
        product.setReviews(productReviews);

        reviewRepo.save(review);
        userRepo.save(user);
        productRepo.save(product);

        return new ResponseEntity<>(review, HttpStatus.CREATED);
    }

    public ResponseEntity<?> updateReview(ObjectId id,ReviewEntity review, String authorization) {
        UserEntity user = getUserByToken.userDetails(authorization);

        if(user == null){
            return new ResponseEntity<>("No User Found",HttpStatus.NOT_FOUND);
        }

        List<ReviewEntity> reviews = user.getReviews();

        if(reviews.isEmpty()){
            return new ResponseEntity<>("No Reviews Found",HttpStatus.NOT_FOUND);
        }

        boolean updated= false;

        Optional<ReviewEntity> actualReview = reviewRepo.findById(id);

        if(actualReview.isEmpty()){
            return new ResponseEntity<>("Review Not found with the given id",HttpStatus.NOT_FOUND);
        }

        for(ReviewEntity reviewEntity : reviews){
            if(reviewEntity.getId().equals(id)){
                updated=true;
                if(review.getRating()!=0) {
                    ProductEntity product = reviewEntity.getProduct();
                    product.setSumOfRatings(product.getSumOfRatings()-reviewEntity.getRating()+review.getRating());
                    product.setAverageRating((float)product.getSumOfRatings()/product.getReviews().size());
                    reviewEntity.setRating(review.getRating());
                    reviewRepo.save(reviewEntity);
                    productRepo.save(product);
                }
                if(review.getDescription()!=null) {
                    reviewEntity.setDescription(review.getDescription());
                    reviewRepo.save(reviewEntity);
                }
                break;
            }
        }

        if(!updated){
            return new ResponseEntity<>("The review doesn't belong to this user",HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>("Updated Review",HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<?> deleteReview(ObjectId id, String authorization) {
        UserEntity user = getUserByToken.userDetails(authorization);

        if(user == null){
            return new ResponseEntity<>("No User Found",HttpStatus.NOT_FOUND);
        }

        List<ReviewEntity> reviews = user.getReviews();

        if(reviews.isEmpty()){
            return new ResponseEntity<>("No Reviews Found",HttpStatus.NOT_FOUND);
        }

        Optional<ReviewEntity> review = reviewRepo.findById(id);

        if(review.isEmpty()){
            return new ResponseEntity<>("No Review found with the given ID",HttpStatus.NOT_FOUND);
        }

        boolean found= false;

        for(ReviewEntity rev: reviews){
            if(rev.getId().equals(review.get().getId())){
                found=true;
                break;
            }
        }

        if(!found){
            return new ResponseEntity<>("You haven't written a review for this product",HttpStatus.UNAUTHORIZED);
        }

        ProductEntity product = review.get().getProduct();

        for(int i=0;i<product.getReviews().size();i++){
            if(product.getReviews().get(i).getId().equals(review.get().getId())){
                product.getReviews().remove(i);
                break;
            }
        }

        product.setSumOfRatings(product.getSumOfRatings()- review.get().getRating());
        if(product.getReviews().isEmpty()){
            product.setAverageRating(0);
        }else {
            product.setAverageRating((float) product.getSumOfRatings() / product.getReviews().size());
        }

        productRepo.save(product);
        reviewRepo.deleteById(id);
        for(int i=0;i<user.getReviews().size();i++){
            if(user.getReviews().get(i).getId().equals(review.get().getId())){
                user.getReviews().remove(i);
                break;
            }
        }
        userRepo.save(user);

        return new ResponseEntity<>("Deleted Review Successfully",HttpStatus.NO_CONTENT);
    }
}
