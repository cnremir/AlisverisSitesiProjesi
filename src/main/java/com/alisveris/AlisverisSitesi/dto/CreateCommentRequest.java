package com.alisveris.AlisverisSitesi.dto;

import com.alisveris.AlisverisSitesi.models.Auction;
import com.alisveris.AlisverisSitesi.models.User;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CreateCommentRequest(
        Auction auction,
        User user,
        String commentText,
        LocalDateTime commentTime,
        Integer parentCommentId



) {
}
