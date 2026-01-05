package com.furniture.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MaterialResponse {
    private String fabricName;
    private String description;
    private Boolean petFriendly;
    private Boolean scratchResistant;
    private List<String> warnings;
}

