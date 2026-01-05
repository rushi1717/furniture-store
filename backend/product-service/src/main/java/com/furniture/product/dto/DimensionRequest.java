package com.furniture.product.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DimensionRequest {
    @NotNull
    private Double widthCm;
    @NotNull
    private Double heightCm;
    @NotNull
    private Double lengthCm;
    private Double seatHeightCm;
}
