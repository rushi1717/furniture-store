package com.furniture.product.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_dimensions")
public class ProductDimension {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double widthCm;
    private Double heightCm;
    private Double lengthCm;
    private Double seatHeightCm;

    @OneToOne
    @JoinColumn(name = "variant_id")
    private ProductVariant variant;
}

