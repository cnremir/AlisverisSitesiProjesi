package com.alisveris.AlisverisSitesi.services;

import com.alisveris.AlisverisSitesi.dto.CreateOfferRequest;
import com.alisveris.AlisverisSitesi.models.*;
import com.alisveris.AlisverisSitesi.repository.OfferRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OfferServices {
    private final OfferRepository offerRepository;
    private final UserServices userServices;

    public OfferServices(OfferRepository offerRepository, UserServices userServices) {
        this.offerRepository = offerRepository;
        this.userServices = userServices;
    }


    public void AddOffer(Offer offer){

        offerRepository.save(offer);

    }

    public Offer getOfferByProductId(Integer productId){
        return offerRepository.getOfferByProductId(productId).orElseThrow(()-> new RuntimeException("Ürün Bulunamadı"));

    }
    public Offer getOfferByProduct(Product product){

        return offerRepository.getOfferByProduct(product).orElse(null);
    }

    public Offer addOffer(CreateOfferRequest createOfferRequest){
        Offer offer = new Offer().builder()
                .amountOffer(createOfferRequest.amountOffer())
                .user(createOfferRequest.user())
                .auction(createOfferRequest.auction())
                .amountOfferTime(createOfferRequest.amountOfferTime())
                .product(createOfferRequest.product())
                .status(createOfferRequest.status())
                .build();


        offerRepository.save(offer);
        return offer;
    }
    public List<Offer> getOffersByStatus(Status status){
        return offerRepository.getOfferByStatus(status);
    }

    public void OncekiKullanicininParasiniVer(Auction auction){
        List< Offer> offers = getOffersByStatus(Status.Active);
        for(Offer offer : offers){
            if(offer.getProduct().equals(auction.getProduct()) && offer.getStatus().equals(Status.Active)){
                User user = offer.getUser();
                user.setBalance(user.getBalance()+offer.getAmountOffer());
                userServices.updateUser(user);
                offer.setStatus(Status.Passive);
                offerRepository.save(offer);
            }
        }

    }

    public Offer deneme(Auction auction){
     return offerRepository.getOfferByAuctionAndStatus(auction,Status.Active).orElse(null);

    }

    public List<Auction> getList(User user){
        return offerRepository.OfferFilter(user);
    }

    public List<Offer> getOfferListByProductId(Integer productId){

        return offerRepository.getOfferListByProductId(productId);
    }

    public List<Offer> getFinishedAuctionFromOffer(User user){

        return offerRepository.getFinishedOfferList(user);

    }

    public List<Offer> getContinuingOffer(User user){

        return offerRepository.getContinuingOffer(user);
    }

@Transactional
    public void deleteOfferByUserId(Integer userId){

        offerRepository.deleteByUserId(userId);
    }
}
