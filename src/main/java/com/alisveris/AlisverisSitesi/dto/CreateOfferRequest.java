package com.alisveris.AlisverisSitesi.dto;

import com.alisveris.AlisverisSitesi.models.Auction;
import com.alisveris.AlisverisSitesi.models.Product;
import com.alisveris.AlisverisSitesi.models.Status;
import com.alisveris.AlisverisSitesi.models.User;
import lombok.Builder;

import java.time.LocalDateTime;
@Builder
public record CreateOfferRequest(
        User user,
        Double amountOffer,
        LocalDateTime amountOfferTime,
        Auction auction,
        Product product,
        Status status
) {

}
