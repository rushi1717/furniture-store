package com.furniture.product.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PackageResponse {

    private String boxNumber;
    private Double widthCm;
    private Double heightCm;
    private Double lengthCm;
    private Double weightKg;

    // getters & setters
}
