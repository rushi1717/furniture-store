package com.furniture.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class VariantImageResponse {
    private String primary;
    private String secondary;
    private String dimension;
    private String variant;
    private List<String> gallery;
}
