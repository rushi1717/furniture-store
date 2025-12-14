package com.furniture.product.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

    private Long id;
    private String name;
    private String category;
    private String shortDescription;
    private String longDescription;
    private String referenceNumber;

    private Boolean hasStorage;
    private Integer deliveryMinWeeks;
    private Integer deliveryMaxWeeks;

    private String slug;
    private String status;

    private List<VariantResponse> variants;
    private List<ProductImageResponse> images;

}
