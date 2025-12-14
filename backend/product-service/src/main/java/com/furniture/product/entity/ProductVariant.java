package com.furniture.product.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_variants")
public class ProductVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String size;
    private Double price;
    private String currency;
    private String fabricName;
    private String fabricCode;
    private String color;
    private Boolean isDefault;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    private String variantSlug;

    @OneToOne(mappedBy = "variant", cascade = CascadeType.ALL)
    private ProductDimension dimension;

    @OneToMany(mappedBy = "variant", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductPackage> packages = new HashSet<>();
}
