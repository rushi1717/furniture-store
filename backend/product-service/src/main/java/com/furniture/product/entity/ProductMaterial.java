package com.furniture.product.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_materials")
public class ProductMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fabricName;     // Monet
    private String description;    // Long explanation text
    private Boolean petFriendly;
    private Boolean scratchResistant;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}

