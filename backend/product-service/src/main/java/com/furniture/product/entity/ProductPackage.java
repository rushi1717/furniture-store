package com.furniture.product.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_packages")
public class ProductPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String boxNumber;   // Box N°1
    private Double widthCm;
    private Double heightCm;
    private Double lengthCm;
    private Double weightKg;

    @ManyToOne
    @JoinColumn(name = "variant_id",nullable = false)
    private ProductVariant variant;
}

