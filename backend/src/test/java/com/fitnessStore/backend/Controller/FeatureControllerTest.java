package com.fitnessStore.backend.Controller;

import com.fitnessStore.backend.Entity.FeatureEntity;
import com.fitnessStore.backend.Repository.FeatureRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class FeatureControllerTest {
    @Mock
    private FeatureRepo featureRepo;

    @InjectMocks
    private FeatureController featureController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    public void testUploadImage(Map<String,String> credentials) {
//        FeatureEntity feature = FeatureEntity.builder()
//                .image(credentials.get("image"))
//                .build();
//
//        when(featureRepo.save(any(FeatureEntity.class))).thenReturn(feature);
//
//        ResponseEntity<?> result = featureController.UploadImage(Map.of());
//
//        assertEquals(HttpStatus.CREATED, result.getStatusCode());
//        Map<String, Object> responseBody = (Map<String, Object>) result.getBody();
//        assertTrue((Boolean) responseBody.get("success"));
//        assertEquals(feature, responseBody.get("data"));
//        verify(featureRepo, times(1)).save(feature);
//    }

    @Test
    public void testGetImages() {
        FeatureEntity feature1 = FeatureEntity.builder().image("sample1").build();
        FeatureEntity feature2 = FeatureEntity.builder().image("sample2").build();
        List<FeatureEntity> features = Arrays.asList(feature1, feature2);

        when(featureRepo.findAll()).thenReturn(features);

        ResponseEntity<?> result = featureController.GetImages();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) result.getBody();
        assertTrue((Boolean) responseBody.get("success"));
        assertEquals(features, responseBody.get("data"));
        verify(featureRepo, times(1)).findAll();
    }
}