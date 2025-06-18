package com.alisveris.AlisverisSitesi.repository;

import com.alisveris.AlisverisSitesi.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface OfferRepository extends JpaRepository<Offer,Integer > {

    @Query("SELECT o FROM Offer o WHERE o.product.productId = :productId")
    Optional<Offer> getOfferByProductId(@Param("productId") Integer productId);

    Optional <Offer> getOfferByProduct(Product product);

    List<Offer> getOfferByStatus(Status status);

    @Query("SELECT o from Offer o JOIN o.auction oa where " +
            "o.auction=:auction AND o.status =:status")
    Optional<Offer> getOfferByAuctionAndStatus(@Param("auction") Auction auction, @Param("status") Status status);

    @Query("SELECT o.auction FROM Offer o WHERE o.user = :user AND o.status = 'Active'")
    List<Auction> OfferFilter(@Param("user") User user);


    @Query("SELECT o FROM Offer o WHERE o.product.productId = :productId order by o.amountOfferTime")
    List<Offer> getOfferListByProductId(@Param("productId") Integer productId);

    @Query("Select of FROM Offer of where of.auction.status = 'Finish' AND " +
            "of.status= 'Active' AND " +
            "of.user = :user")
    List<Offer> getFinishedOfferList(@Param("user") User user);

    @Query("Select of FROM Offer of where of.auction.status = 'Active' AND " +
            "of.status= 'Active' AND " +
            "of.user = :user")
    List<Offer> getContinuingOffer(@Param("user") User user);

    @Modifying
    @Transactional
    @Query("DELETE FROM Offer of where of.user.userId = :userId")
    public void deleteByUserId(@Param("userId") Integer userId);


    @Query("SELECT of.auction from Offer of where of.user = :user AND " +
            "of.status = 'Active'")
    List<Auction> findProductsOnOffer(@Param("user") User user);

}
