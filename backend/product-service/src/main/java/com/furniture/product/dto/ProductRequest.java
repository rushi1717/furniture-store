package com.furniture.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
public class ProductRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String category;

    private String shortDescription;
    private String longDescription;

    @NotBlank
    private String referenceNumber;

    @NotNull
    private Boolean hasStorage;

    @NotNull
    private Integer deliveryMinWeeks;

    @NotNull
    private Integer deliveryMaxWeeks;

    @NotBlank
    private String slug;

    @NotNull
    private List<VariantRequest> variants;

//    @NotNull
//    private List<ImageRequest> images;

    // getters & setters
}

