package com.furniture.product.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DimensionResponse {

    private Double widthCm;
    private Double heightCm;
    private Double lengthCm;
    private Double seatHeightCm;

    // getters & setters
}
