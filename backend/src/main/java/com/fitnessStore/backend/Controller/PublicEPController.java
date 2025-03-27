package com.fitnessStore.backend.Controller;

import com.fitnessStore.backend.apiServices.PublicEPServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product")
public class PublicEPController {

    @Autowired
    private PublicEPServices publicEPServices;

    @GetMapping("/{category}")
    public ResponseEntity<?> getProducts(@PathVariable String category){
        return publicEPServices.getProducts(category);
    }

    @GetMapping
    public ResponseEntity<?> getProductByTitle(@RequestParam String title){
        return publicEPServices.getProductByTitle(title);
    }
}
