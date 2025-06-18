package com.alisveris.AlisverisSitesi.services;

import com.alisveris.AlisverisSitesi.models.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BidService {

    private final UserServices userServices;
    private final ProductServices productServices;
    private final OfferServices offerServices;
    private final AuctionServices auctionServices;


    public BidService(UserServices userServices, ProductServices productServices, OfferServices offerServices, AuctionServices auctionServices) {
        this.userServices = userServices;
        this.productServices = productServices;
        this.offerServices = offerServices;
        this.auctionServices = auctionServices;
    }


    //Yeni teklif geldiginde
    public void makeOffer(Integer userId, Integer productId, Double newPrice) {
        Product product = productServices.getProduct(productId);
        User user = userServices.getUser(userId);

        Offer offer = offerServices.getOfferByProductId(productId);


        Auction auction = auctionServices.getAuction(productId);
        Double oldPrice = auction.getCurrentPrice();
        Double accrual = newPrice - oldPrice;
        auction.setAccrual(accrual);
        auction.setCurrentPrice(newPrice);  //Yeni miktarı güncelledim
        //Artış miktarını kaydettim

        Offer ox = new Offer().builder().user(user)
                .amountOffer(newPrice)
                .amountOfferTime(LocalDateTime.now())
                .product(product)
                .auction(auction).build();

        offerServices.AddOffer(ox);
        auctionServices.addNewAction(auction); //Güncelledik
    }


    //Ürün oluşturuldugunda ilk defa kaydetmek içib
    public void FirstOffer(Integer userId, Integer productId, Double price, LocalDateTime finishTime) {
        Auction auction = new Auction().builder()
                .startingPrice(price)
                .startingTime(LocalDateTime.now())
                .finishTime(finishTime)
                .status(Status.Active)
                .build();

//Veritabanına ekleme
        auctionServices.addNewAction(auction);

        Offer newOffer = new Offer().builder().
                user(userServices.getUser(userId))
                .product(productServices.getProduct(productId))
                .amountOffer(price)
                .amountOfferTime(LocalDateTime.now())
                .auction(auction)
                .build();

//Veritabanına ekleme
        offerServices.AddOffer(newOffer);


    }
}
