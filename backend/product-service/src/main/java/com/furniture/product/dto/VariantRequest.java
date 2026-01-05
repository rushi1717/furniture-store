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
public class VariantRequest {
    @NotBlank
    private String size;
    @NotNull
    private Double price;
    @NotNull
    private String description;
    @NotBlank
    private String currency;
    @NotBlank
    private String fabricName;
    @NotBlank
    private String fabricCode;
    @NotBlank
    private String color;
    @NotNull
    private Boolean isDefault;
    @NotBlank
    private String variantSlug;
    @NotNull
    private DimensionRequest dimension;
    @NotNull
    private List<PackageRequest> packages;
    private Integer initialStock;
    @NotNull
    private MaterialRequest material;
    @NotNull
    private Double productWeightKg;
    @NotNull
    private Double maxSupportedWeightKg;
    @NotNull
    private Double totalWeightWithPackagingKg;
}

