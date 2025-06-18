package com.alisveris.AlisverisSitesi.dto;

import com.alisveris.AlisverisSitesi.models.Product;
import com.alisveris.AlisverisSitesi.models.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Builder
public record CreateAuctionReques(
        Product product,
        Double startingPrice,
        Double currentPrice,


        LocalDateTime startingTime,

        LocalDateTime finishTime,


        Status status,

        Double accrual,
        long kalanSure,
        boolean showBox,
        String boxMessage


) {
}
