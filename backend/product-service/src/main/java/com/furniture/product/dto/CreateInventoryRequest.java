package com.furniture.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateInventoryRequest {
    private Long productId;
    private Long variantId;
    private Integer totalStock;
}
