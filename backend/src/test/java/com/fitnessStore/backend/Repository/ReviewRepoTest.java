package com.fitnessStore.backend.Repository;

import com.fitnessStore.backend.Entity.ProductEntity;
import com.fitnessStore.backend.Entity.ReviewEntity;
import com.fitnessStore.backend.Entity.UserEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class ReviewRepoTest {
    @Autowired
    ReviewRepo reviewRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    ProductRepo productRepo;

    private ReviewEntity review;

    @BeforeEach
    public void setup() {
        UserEntity user = UserEntity.builder()
                .username("testUser")
                .email("test@example.com")
                .mobile("999999999")
                .role("USER")
                .build();
        userRepo.save(user);

        ProductEntity product = ProductEntity.builder()
                .description("testDescription")
                .price(100.0)
                .build();
        productRepo.save(product);

        review = ReviewEntity.builder()
                .user(user)
                .product(product)
                .userId(user.getId())
                .userName(user.getUsername())
                .productId(product.getId())
                .reviewMessage("Great product!")
                .reviewValue(5.0)
                .build();
        reviewRepo.save(review);
    }

    @Test
    void findByProductId() {
        List<ReviewEntity> foundReviews = reviewRepo.findByProductId(review.getProductId());
        assertEquals(1, foundReviews.size());
        assertThat(foundReviews.get(0).getReviewMessage()).isEqualTo("Great product!");
    }

    @AfterEach
    void tearDown(){
        userRepo.deleteAll();
        productRepo.deleteAll();
        reviewRepo.deleteAll();
    }
}