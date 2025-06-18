package com.alisveris.AlisverisSitesi.dto;

import com.alisveris.AlisverisSitesi.models.Auction;
import com.alisveris.AlisverisSitesi.models.Status;
import com.alisveris.AlisverisSitesi.models.User;
import lombok.Builder;

@Builder
public record CreateRequest(
        Integer requestId,
        Auction auction,
        User user,
        Status status



) {
}
