package com.furniture.product.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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

    private String fabricName;
    @Column(length = 5000)
    private String description;

    private Boolean petFriendly;
    private Boolean scratchResistant;

    @ElementCollection
    private List<String> warnings;

    @OneToOne
    @JoinColumn(name = "variant_id", nullable = false)
    private ProductVariant variant;
}
