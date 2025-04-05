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

        config.put("cloud_name","dvv2n0tit");
        config.put("api_key","437319582694656");
        config.put("api_secret","RuKh18bj6edbay3FsCZ-RRkcA5M");
        config.put("secure",true);

        return new Cloudinary(config);
    }
}
