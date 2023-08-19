package com.project.un_site_de_planification_et_de_suivi_de_projets.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
 
 
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        exposeDirectory("users-photos", registry);
    }
     
    private void exposeDirectory(String dirName, ResourceHandlerRegistry registry) {
        Path dirPath = Path.of(dirName);
        Path uploadDir = dirPath.toAbsolutePath().normalize();

         
        registry.addResourceHandler("/" + dirPath.normalize() + "/**").addResourceLocations("file:/"+ uploadDir + "/");
    }
}