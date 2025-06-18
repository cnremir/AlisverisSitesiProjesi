package com.alisveris.AlisverisSitesi.dto;

import com.alisveris.AlisverisSitesi.models.Auction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor

public class AuctionDTO {
    private Auction auction;
    private String base64Image;

    public AuctionDTO(Auction auction){
        this.auction = auction;
    }

    public Auction getAuction() {
        return auction;
    }

    public void setAuction(Auction auction) {
        this.auction = auction;
    }

    public String getBase64Image() {
        return base64Image;
    }

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }
}
