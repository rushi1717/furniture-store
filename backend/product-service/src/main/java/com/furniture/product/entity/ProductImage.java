package com.furniture.product.entity;

import com.furniture.product.enums.ImageType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "product_images",
        uniqueConstraints = {
                // prevent duplicate same image
                @UniqueConstraint(
                        columnNames = {"variant_id", "image_url", "image_type"},
                        name = "uk_variant_image"
                )
        }
)
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String imageUrl;
    @Enumerated(EnumType.STRING)
    private ImageType imageType;
    @Column(nullable = false)
    private Integer position;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_id", nullable = false)
    private ProductVariant variant;
}
