package com.furniture.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
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
    @NotBlank
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
    @NotBlank
    private String productType;
    @NotNull
    private Boolean isAvailable;
    @NotNull
    private List<String> features;
    @NotBlank
    private String producerEmail;
    @NotBlank
    private String ean;
}

