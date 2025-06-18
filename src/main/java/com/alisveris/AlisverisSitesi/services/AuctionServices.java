package com.alisveris.AlisverisSitesi.services;

import com.alisveris.AlisverisSitesi.dto.AuctionDTO;
import com.alisveris.AlisverisSitesi.dto.CreateAuctionReques;
import com.alisveris.AlisverisSitesi.dto.CreateRequest;
import com.alisveris.AlisverisSitesi.models.*;
import com.alisveris.AlisverisSitesi.repository.AuctionRepository;
import com.alisveris.AlisverisSitesi.repository.OfferRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;

@Service
public class AuctionServices {

    private final AuctionRepository auctionRepository;
    private final RequestServices requestServices;
private final ProductServices productServices;
private final UserServices userServices;
private final OfferServices offerServices;
private final OfferRepository offerRepository;

private final ImageService imageService;
    public AuctionServices(AuctionRepository auctionRepository, RequestServices requestServices, ProductServices productServices, UserServices userServices, OfferServices offerServices, OfferRepository offerRepository, ImageService imageService) {
        this.auctionRepository = auctionRepository;
        this.requestServices = requestServices;

        this.productServices = productServices;
        this.userServices = userServices;
        this.offerServices = offerServices;
        this.offerRepository = offerRepository;
        this.imageService = imageService;
    }


    public void addNewAction(Auction auction){

       auctionRepository.saveAndFlush(auction);


    }

    public Auction getAuction(Integer productId){
        return auctionRepository.getAuction(productId);

    }
    public void addAuction(CreateAuctionReques createAuctionReques){
    Auction auction = new Auction().builder()
            .product(createAuctionReques.product())
            .startingPrice(createAuctionReques.startingPrice())
            .finishTime(createAuctionReques.finishTime())
            .currentPrice(createAuctionReques.currentPrice())
            .startingTime(createAuctionReques.startingTime())
            .status(createAuctionReques.status())
            .showBox(createAuctionReques.showBox())
            .boxMessage(createAuctionReques.boxMessage())
            .accrual(createAuctionReques.accrual())
            .build();

        auctionRepository.save(auction);

        CreateRequest createRequest = CreateRequest.builder()
                .auction(auction)
                .user(auction.getProduct().getUser())
                .status(Status.Passive)
                .build();

        requestServices.createRequest(createRequest);

    }
    public Auction getAutionByProduct(Integer productId){
       return auctionRepository.getAuctionByProductProductId(productId).orElse(null);
    }

    public List<Auction> auctionListbyStatus(Status status){
        return auctionRepository.getAuctionByStatus(status);
    }

    public List<Auction> getAllAuction(){
        return auctionRepository.findAll();

    }
    public long kalanSureHesapla(Auction auction) {

        LocalDateTime timeNow = LocalDateTime.now();
        LocalDateTime finishTime = auction.getFinishTime();

        if (finishTime == null) {
            return 0;
        }
        if (Duration.between(timeNow, finishTime).getSeconds() <= 0) {
            return -1;
        }

        return Duration.between(timeNow, finishTime).getSeconds();
    }

    public long kalanSureHesaplaAuctionDto(AuctionDTO auctionDTO) {

        LocalDateTime timeNow = LocalDateTime.now();
        LocalDateTime finishTime = auctionDTO.getAuction().getFinishTime();

        if (finishTime == null) {
            return 0;
        }
        if (Duration.between(timeNow, finishTime).getSeconds() <= 0) {
            return -1;
        }

        return Duration.between(timeNow, finishTime).getSeconds();
    }

    @Scheduled(fixedRate = 1000)
    public void suresiBitmisUrunleriCek() {
        List<Auction> tumAuctions = getAllAuction();

        Iterator<Auction> iterator = tumAuctions.iterator();

        while (iterator.hasNext()) {
            Auction auction = iterator.next();
            if (kalanSureHesapla(auction) == -1) {
                if(auction.getStatus().equals(Status.Active)){
                    auction.setStatus(Status.Passive);
                    islemBittiParayiArttir(auction);


                }
                iterator.remove();
            }
            auction.setKalanSure(kalanSureHesapla(auction));
        }

    }


    public void islemBittiParayiArttir(Auction auction) {
            Product product = productServices.getProduct(auction.getAuctionId());
            User user = product.getUser();
            user.setBalance(auction.getCurrentPrice()+user.getBalance());
            auction.setStatus(Status.Finish);
            userServices.updateUser(user);
            auctionRepository.save(auction);
    }

public List<Auction> filterAuctionList(List<String> category, LocalDateTime endTime, Double minPrice, Double maxPrice,User user){
        return auctionRepository.filterAuctions(category,endTime,minPrice,maxPrice,user);
}

public void deleteAuction(Auction auction){
        auctionRepository.delete(auction);

}

public void enYuksekTeklifKontrol(Auction auction){
    Offer offer = offerServices.deneme(auction);
    if(offer != null){
        if(userServices.activeUser().equals(offer.getUser())){
            auction.setBoxMessage("Bu ürüne En yüksek teklif veren sizsiniz");
            auction.setShowBox(true);
        }
        else{
            auction.setShowBox(false);
        }
    }
}

public List<Auction> getAuctionByUser(User user){
        return auctionRepository.getAuctionByUser(user);
}

public List<Auction> getAuctionForProfilePage(User user){
    return auctionRepository.getAuctionByStatus(user);


}
    public String encodeImageToBase64(byte[] imageBytes) {
        return Base64.getEncoder().encodeToString(imageBytes);
    }


public AuctionDTO auctionDTOList(Auction auction) throws SQLException, IOException {
        AuctionDTO auctionDTO = new AuctionDTO(auction);
    byte[] bytes = imageService.displayImage(auction.getProduct().getProductId());
    if(bytes != null){
        String s = encodeImageToBase64(bytes);
        auctionDTO.setBase64Image(s);
    }
    return auctionDTO;

}
    public List<AuctionDTO> auctionToAuctiohDTO(List<Auction> auctions) throws SQLException, IOException {

        List<AuctionDTO> auctionDTOS = new ArrayList<>();
        for(Auction auction: auctions){
            auctionDTOS.add(auctionDTOList(auction));
        }

        return auctionDTOS;
    }
    @Transactional
    public void deleteAuctionByProductId(Product product){
        if(product != null){
            auctionRepository.deleteAuctionByProductId(product.getProductId());
        }
    }

    public List<Auction> findProductsOnOffer(User user){
        return offerRepository.findProductsOnOffer(user);
    }

    public List<Auction> getAuctionByStatusAndUser(User user,Status status){
        return auctionRepository.getAuctionByStatusAndUser(user,status);


    }

    public List<Auction> getAuctionBySubCategory(Integer subCategoryId,Auction auction,User user){
        return auctionRepository.getAuctionBySubCategory(subCategoryId,auction,user);
    }



}

