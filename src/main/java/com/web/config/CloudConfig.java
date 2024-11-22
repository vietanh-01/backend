package com.web.config;


import com.cloudinary.Cloudinary;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@SpringBootApplication
public class CloudConfig {

    @Bean
    public Cloudinary cloudinaryConfigs() {
        Cloudinary cloudinary = null;
        Map config = new HashMap();
        config.put("cloud_name", "dlkhzppmb");
        config.put("api_key", "244632829635627");
        config.put("api_secret", "3hGOeefXOONRVH-NTQyteQgeX08");
        cloudinary = new Cloudinary(config);
        return cloudinary;
    }

}
