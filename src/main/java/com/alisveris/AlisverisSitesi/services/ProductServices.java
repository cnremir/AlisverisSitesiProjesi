package com.alisveris.AlisverisSitesi.services;

import com.alisveris.AlisverisSitesi.dto.CreateProductRequest;
import com.alisveris.AlisverisSitesi.models.*;
import com.alisveris.AlisverisSitesi.repository.ProductRepository;
import com.alisveris.AlisverisSitesi.specifications.CommentSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServices {

    private final CommentSpecification commentSpecification;
    private final ProductRepository productRepository;
    private final String FILE_PATH = "C:\\Users\\Emir\\Desktop\\Resimler";

    @Autowired
    public ProductServices(CommentSpecification commentSpecification, ProductRepository productRepository) {
        this.commentSpecification = commentSpecification;
        this.productRepository = productRepository;
    }


    public Product getProduct (Integer productId){

        return productRepository.getProductsByProductId(productId).orElse(null);

    }

    public Product addProduct(CreateProductRequest request) throws IOException{
        Product product = new Product().builder()
                .productName(request.productName())
                .productPrice(request.productPrice())
                .productDescripton(request.productDescripton())
                .category(request.category())
                .subCategory(request.subCategory())
                .user(request.user())
                .build();


        productRepository.save(product);
        System.out.println("product kayıt edildi");
        return product;

    }
    public void deleteProduct(Product product){
        productRepository.delete(product);

    }
    public Product getProductByProductName(String productName){
        return productRepository.getProductsByProductName(productName).orElse(null);
    }

public Product getProductByUser(User user){
        return productRepository.getProductsByUser(user).orElse(null);
}



    public String uploadImageFileSystem(MultipartFile multipartFile) throws IOException {
        String filePath = FILE_PATH+multipartFile.getOriginalFilename();


     //   ImagesFile imagesFile = new ImagesFile().builder()
       //         .imagePath(filePath).build();
        multipartFile.transferTo(new File(filePath));
        return filePath;


    }




public void updateProduct(Product product){
        productRepository.save(product);
}

    @Transactional
public void deleteProductByUserId(Integer userId){
        productRepository.deleteByUserId(userId);
}


    public List<Product> getAllProductByUser(User user){
        return productRepository.getAllProductsByUser(user);
}


    @Transactional
    public void deleteProductByProduct(Product product){
        productRepository.deleteProductByProduct(product);
}
}


