package com.alisveris.AlisverisSitesi.services;

import com.alisveris.AlisverisSitesi.models.Auction;
import com.alisveris.AlisverisSitesi.models.Status;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class    AdminServices {


    private final RequestServices requestServices;
    private final AuctionServices auctionServices;

    public AdminServices(RequestServices requestServices, AuctionServices auctionServices) {
        this.requestServices = requestServices;

        this.auctionServices = auctionServices;
    }

    public List<Auction> requestsList() {
        return auctionServices.auctionListbyStatus(Status.Pending);
    }



}
