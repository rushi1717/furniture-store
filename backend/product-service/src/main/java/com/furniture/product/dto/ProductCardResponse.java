package com.furniture.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProductCardResponse {
    private Long id;
    private String name;
    private String category;
    private Double price;
    private String primaryImg;
    private String secondaryImg;
    private String slug;
    private String description;
    private String variantSlug;
}
