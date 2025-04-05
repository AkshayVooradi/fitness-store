package com.fitnessStore.backend.Controller;

import com.fitnessStore.backend.apiServices.PublicEPServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class PublicEPController {

    @Autowired
    private PublicEPServices publicEPServices;

    @GetMapping("/get")
    public ResponseEntity<?> getProducts(@RequestParam(required = false) List<String> category,
                                         @RequestParam(required = false) List<String> brand,
                                        String sortBy){
        return publicEPServices.getProducts(category,brand,sortBy);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductByTitle(@PathVariable String id){
        return publicEPServices.getProductByID(id);
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<?> searchProduct(@PathVariable String keyword){
        return publicEPServices.search(keyword);
    }
}
