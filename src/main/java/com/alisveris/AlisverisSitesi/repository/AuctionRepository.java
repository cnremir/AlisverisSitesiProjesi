package com.alisveris.AlisverisSitesi.repository;

import com.alisveris.AlisverisSitesi.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface AuctionRepository extends JpaRepository<Auction,Integer> {
    @Query("SELECT c FROM Auction c where c.product.productId=:productId")
    Auction getAuction(@Param("productId") Integer productId);

    List<Auction> getAuctionByStatus(Status status);

    Optional<Auction> getAuctionByProductProductId(Integer productId);

   List<Auction> findAll();

    @Query("SELECT a FROM Auction a JOIN a.product ap WHERE " +
            "(:category IS NULL OR ap.category.categoryName IN :category) AND " +
            "(:endTime IS NULL OR a.finishTime <= :endTime) AND " +
            "(:minPrice IS NULL OR (CASE WHEN a.currentPrice = 0 THEN a.startingPrice ELSE a.currentPrice END) >= :minPrice) AND " +
            "(:maxPrice IS NULL OR (CASE WHEN a.currentPrice = 0 THEN a.startingPrice ELSE a.currentPrice END) <= :maxPrice) AND " +
            "(:user IS NULL OR ap.user = :user) AND " +
            "a.status = 'Active'")
    List<Auction> filterAuctions(
            @Param("category") List<String> category,
            @Param("endTime") LocalDateTime endTime,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("user") User user);

    @Query("SELECT a FROM Auction a Where a.product.user = :user")
    List<Auction> getAuctionByUser(@Param("user") User user);

    @Query("SELECT a From Auction a Where a.product.user = :user AND " +
            "a.status='Finish' ")
    List<Auction> getAuctionByStatus(@Param("user") User user);


    @Query("SELECT a From Auction a Where a.product.user != :user AND " +
            "a.status= :status ")
    List<Auction> getAuctionByStatusAndUser(@Param("user") User user,@Param("status") Status status);



    @Transactional
    @Modifying
    @Query("DELETE FROM Auction au where au.product.productId = :productId")
    void deleteAuctionByProductId(@Param("productId") Integer productId);



    @Query("SELECT ac FROM Auction ac where ac.product.subCategory.subCategoryId = :subcategoryId AND " +
            "ac.status = 'Active' AND ac != :auction AND ac.product.user != :user")
    List<Auction> getAuctionBySubCategory(@Param("subcategoryId") Integer subcategoryId,@Param("auction")Auction auction,@Param("user") User user);

}

