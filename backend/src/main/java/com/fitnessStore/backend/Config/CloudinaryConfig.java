package com.fitnessStore.backend.Config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary getCloudinary(){

        Map config = new HashMap();

        config.put("cloud_name","dnxqfrnzv");
        config.put("api_key","266513594295381");
        config.put("api_secret","XXP80ValtUHvX0QdAD6ta4-iKyM");
        config.put("secure",true);

        return new Cloudinary(config);
    }
}
