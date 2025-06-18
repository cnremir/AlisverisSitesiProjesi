package com.alisveris.AlisverisSitesi.services;

import com.alisveris.AlisverisSitesi.models.Images;
import com.alisveris.AlisverisSitesi.models.Product;
import com.alisveris.AlisverisSitesi.repository.ImageRepository;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@Service
public class ImageService {
    private final ImageRepository imageRepository;


    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public Images create(Images image) {
        return imageRepository.save(image);
    }

    public List<Images> viewAll() {
        return imageRepository.findAll();
    }
    public Images viewById(Integer id) {
        return imageRepository.findById(id).get();
    }
    public Images findImagesByProductId(Integer productId){
        return imageRepository.getAllImagesByProductId(productId);
    }
    public byte[] displayImage(Integer id) throws IOException, SQLException
    {
        Images image = findImagesByProductId(id);
        byte [] imageBytes = null;
        imageBytes = image.getImage().getBytes(1,(int) image.getImage().length());
        return  imageBytes;
    }

    @Transactional
    public void deleteImagesByProductId(Product product){
        if(product != null){
            imageRepository.deleteImagesByProductId(product.getProductId());
        }

    }

}
