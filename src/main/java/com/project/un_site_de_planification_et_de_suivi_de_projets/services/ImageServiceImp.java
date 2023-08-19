package com.project.un_site_de_planification_et_de_suivi_de_projets.services;

import com.project.un_site_de_planification_et_de_suivi_de_projets.entities.Image;
import com.project.un_site_de_planification_et_de_suivi_de_projets.repos.ImageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@AllArgsConstructor
public class ImageServiceImp implements ImageService {
    private final ImageRepository imageRepository;

    //TODO Complete this method
    @Override
    public Image getImage(String id) {
        return imageRepository.findById(id).orElse(null);
    }

    @Override
    public Image addImage(MultipartFile image) throws IOException {
        String fileName = StringUtils.cleanPath(image.getOriginalFilename());
        String fileType = image.getContentType();
        byte[] data = image.getBytes();
        Image img = new Image(null, fileName, fileType, data);
        return imageRepository.save(img);
    }

    @Override
    public Image addImageLa(Image image) {
        return imageRepository.save(image);
    }
}
