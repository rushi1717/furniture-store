package com.furniture.product.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VariantResponse {

    private Long id;
    private String size;
    private Double price;
    private String currency;

    private String fabricName;
    private String fabricCode;
    private String color;

    private Boolean isDefault;
    private String variantSlug;

    private DimensionResponse dimension;
    private List<PackageResponse> packages;

    // getters & setters
}
