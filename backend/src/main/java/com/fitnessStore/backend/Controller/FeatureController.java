package com.fitnessStore.backend.Controller;

import com.fitnessStore.backend.Entity.FeatureEntity;
import com.fitnessStore.backend.Repository.FeatureRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/feature")
public class FeatureController {

    @Autowired
    private FeatureRepo featureRepo;

    @PostMapping("/add")
    public ResponseEntity<?> UploadImage(){

        FeatureEntity feature = FeatureEntity.builder()
                .image("sample")
                .build();

        featureRepo.save(feature);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("success",true);
        responseBody.put("data",feature);

        return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
    }

    @GetMapping("/get")
    public ResponseEntity<?> GetImages(){

        List<FeatureEntity> features = featureRepo.findAll();

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("success",true);
        responseBody.put("data",features);

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}
