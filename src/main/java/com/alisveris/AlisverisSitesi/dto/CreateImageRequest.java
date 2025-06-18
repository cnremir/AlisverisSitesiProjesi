package com.alisveris.AlisverisSitesi.dto;

import lombok.Builder;

@Builder
public record CreateImageRequest (

        String imageName,
        String imageType,
        String imagePath

){
}
