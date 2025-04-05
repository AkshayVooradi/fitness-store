package com.fitnessStore.backend.Services;

import com.cloudinary.Cloudinary;
import com.fitnessStore.backend.ExceptionHandling.ResourceNotFoundException;
import com.fitnessStore.backend.Repository.CloudinaryImageUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService implements CloudinaryImageUpload {

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public Map upload(MultipartFile file) {
        try {

            Map data = cloudinary.uploader().upload(file.getBytes(), Map.of());

            return data;

        }catch (IOException e){
            throw new ResourceNotFoundException("Image Uploading failed");
        }
    }
}
