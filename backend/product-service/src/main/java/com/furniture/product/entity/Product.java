package com.furniture.product.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String productType;        // Bed frames
    private Boolean isAvailable;
    private String category;
    @Column(length = 1000)
    private String shortDescription;
    @Column(length = 5000)
    private String longDescription;
    private String referenceNumber;
    private Boolean hasStorage;
    private Integer deliveryMinWeeks;
    private Integer deliveryMaxWeeks;
    @Column(unique = true)
    private String slug;
    private String status;
    @ElementCollection
    private List<String> features;
    private String producerEmail;
    private String ean;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductVariant> variants = new HashSet<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
