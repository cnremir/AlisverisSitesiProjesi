package com.alisveris.AlisverisSitesi.repository;

import com.alisveris.AlisverisSitesi.models.Category;
import com.alisveris.AlisverisSitesi.models.Product;
import com.alisveris.AlisverisSitesi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long>, JpaSpecificationExecutor<Product> {
    Optional<Product> getProductsByProductId(Integer productId);
    Optional<Product> getProductsByProductName(String productName);
    Optional<Product> getProductsByUser(User user);

    @Query("SELECT pr FROM Product pr where pr.user = :user")
    List<Product> getAllProductsByUser(@Param("user") User user);

    @Modifying
    @Transactional
    @Query("DELETE FROM Product pr where pr.user.userId = :userId")
    void deleteByUserId(@Param("userId") Integer userId);


    @Modifying
    @Transactional
    @Query("DELETE FROM Product pr WHERE pr = :product")
    public void deleteProductByProduct(@Param("product") Product product);


}

