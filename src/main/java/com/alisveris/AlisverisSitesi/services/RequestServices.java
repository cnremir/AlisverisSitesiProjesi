package com.alisveris.AlisverisSitesi.services;

import com.alisveris.AlisverisSitesi.dto.CreateRequest;
import com.alisveris.AlisverisSitesi.models.Auction;
import com.alisveris.AlisverisSitesi.models.Requests;
import com.alisveris.AlisverisSitesi.models.Status;
import com.alisveris.AlisverisSitesi.repository.RequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RequestServices {

    private final RequestRepository requestRepository;


    public RequestServices(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    public Requests addRequest(Requests requests){
        requestRepository.save(requests);

        return requests;
    }
    public List<Requests> getAllRequestsByStatus(Status status){

        return requestRepository.findRequestsByStatus(status);
    }

    public void createRequest(CreateRequest createRequest){

        Requests requests = Requests.builder()
                .status(createRequest.status())
                .auction(createRequest.auction())
                .user(createRequest.auction().getProduct().getUser())
                .build();
        requestRepository.save(requests);

    }

    public Requests findRequestByAuction(Auction auction){

        return requestRepository.findRequestsByAuction(auction).orElse(null);
    }

    public void deleteRequests(Requests requests){
        requestRepository.delete(requests);
    }

    @Transactional

    public void deleteRequestByUserId(Integer userId){
        requestRepository.deleteByUserId(userId);
    }

@Transactional
    public void deleteRequestsByAuction(Auction auction){
        requestRepository.deleteRequestsByAuction(auction);
}

}
