package com.furniture.product.dto;

import com.furniture.product.enums.ImageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductImageResponse {

    private String imageUrl;
    private ImageType imageType;

}
