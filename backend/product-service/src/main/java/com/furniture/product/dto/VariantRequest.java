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
public class VariantRequest {

    @NotBlank
    private String size;

    @NotNull
    private Double price;

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

    // getters & setters
}

