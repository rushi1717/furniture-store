package com.furniture.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductCardResponse {

    private Long id;

    private String name;

    private String shortDescription;

    private Double price;

    private String primaryImg;

    private String secondaryImg;

    private String slug;
}
