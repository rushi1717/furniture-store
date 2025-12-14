package com.furniture.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class PackageRequest {

    @NotBlank
    private String boxNumber;

    @NotNull
    private Double widthCm;

    @NotNull
    private Double heightCm;

    @NotNull
    private Double lengthCm;

    @NotNull
    private Double weightKg;

    // getters & setters
}
