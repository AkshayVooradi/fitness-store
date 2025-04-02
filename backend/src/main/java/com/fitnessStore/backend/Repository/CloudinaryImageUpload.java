package com.fitnessStore.backend.Repository;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface CloudinaryImageUpload {
    public Map upload(MultipartFile file);
}
