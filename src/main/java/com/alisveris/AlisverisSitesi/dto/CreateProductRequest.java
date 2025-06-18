package com.alisveris.AlisverisSitesi.dto;

import com.alisveris.AlisverisSitesi.models.*;
import lombok.Builder;

import java.util.List;

@Builder
public record CreateProductRequest(



        String productName,
        Double productPrice,
        String productDescripton,
        Category category,
        SubCategory subCategory,
        Images productImage,
        User user,
        List<Comment> productComments
) {
}
