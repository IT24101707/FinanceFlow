package com.financeflow.financeflow.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // Folder (created next to where the app runs) that stores uploaded profile photos
    public static final String UPLOAD_DIR = "uploads" + File.separator + "profile-photos" + File.separator;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns(
                        "/dashboard",
                        "/income/**",
                        "/expense/**",
                        "/budget/**",
                        "/report/**",
                        "/profile/**"
                )
                .excludePathPatterns(
                        "/api/users/**",
                        "/webjars/**",
                        "/css/**",
                        "/js/**",
                        "/uploads/**"
                );
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + UPLOAD_DIR);
    }

}
