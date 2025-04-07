package com.fitnessStore.backend.Controller;

import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import com.fitnessStore.backend.Entity.ReviewEntity;
import com.fitnessStore.backend.Services.ReviewServices;
import jakarta.servlet.http.HttpServletRequest;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
class ReviewControllerTest {
    @Mock
    private ReviewServices reviewServices;

    @InjectMocks
    private ReviewController reviewController;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateReview() {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("productId", "123");
        credentials.put("reviewMessage", "Great product!");
        credentials.put("reviewValue", "5");

        when(reviewServices.createReview(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> result = reviewController.createReview("token", credentials);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(reviewServices, times(1)).createReview("123", "Great product!", "5", "token");
    }

    @Test
    public void testGetReview() {
        when(reviewServices.getReviewById(anyString(), anyString()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> result = reviewController.getReview("token", "123");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(reviewServices, times(1)).getReviewById("123", "token");
    }
}