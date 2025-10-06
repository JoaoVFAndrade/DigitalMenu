package com.br.digitalmenu.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary(){
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dzzcvnjdp",
                "api_key", "593898329373397",
                "api_secret", "OczEERE7QYHcbBwU7-ZGohKJ1bA"));
    }
}
