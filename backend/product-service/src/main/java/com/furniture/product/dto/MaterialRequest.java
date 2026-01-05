package com.furniture.product.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MaterialRequest {
    @NotBlank
    private String fabricName;
    @NotBlank
    private String description;
    private Boolean petFriendly;
    private Boolean scratchResistant;
    private List<String> warnings;
}

