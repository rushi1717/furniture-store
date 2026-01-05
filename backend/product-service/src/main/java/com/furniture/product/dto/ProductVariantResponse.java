package com.furniture.product.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductVariantResponse {
    private Long productId;
    private Long variantId;
    private String productName;
    private String size;
    private Double price;
    private String currency;
    private String color;
    private String primaryImageUrl; // 🔥 IMPORTANT

}

