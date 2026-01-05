package com.furniture.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private String productType;
    private String category;
    private String shortDescription;
    private String longDescription;
    private String referenceNumber;
    private Boolean hasStorage;
    private Boolean isAvailable;
    private Integer deliveryMinWeeks;
    private Integer deliveryMaxWeeks;
    private String slug;
    private String status;
    private List<String> features;
    private String producerEmail;
    private String ean;
    private List<VariantResponse> variants;
}
