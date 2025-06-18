package com.alisveris.AlisverisSitesi.repository;

import com.alisveris.AlisverisSitesi.models.Images;
import com.alisveris.AlisverisSitesi.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ImageRepository extends JpaRepository<Images,Integer> {

        @Query("SELECT x FROM Images x WHERE x.product.productId  =:productId")
        Images getAllImagesByProductId(@Param("productId") Integer productId);

        @Transactional
        @Modifying
        @Query("DELETE FROM Images im where im.product.productId =:productId")
        void deleteImagesByProductId(@Param("productId") Integer productId);
}
