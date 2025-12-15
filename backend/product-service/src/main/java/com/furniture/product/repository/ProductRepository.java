package com.furniture.product.repository;

import com.furniture.product.dto.ProductCardResponse;
import com.furniture.product.entity.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

public interface ProductRepository extends JpaRepository<Product,Long> {
    @Query("""
    SELECT DISTINCT p FROM Product p
    LEFT JOIN FETCH p.images
    LEFT JOIN FETCH p.variants v
    LEFT JOIN FETCH v.dimension
    LEFT JOIN FETCH v.packages
""")
    List<Product> findAllWithDetails();


    @Query("""
SELECT new com.furniture.product.dto.ProductCardResponse(
    p.id,
    p.name,
    p.category,
    v.price,
    MAX(CASE WHEN img.imageType = com.furniture.product.enums.ImageType.PRIMARY THEN img.imageUrl ELSE null END),
    MAX(CASE WHEN img.imageType = com.furniture.product.enums.ImageType.SECONDARY THEN img.imageUrl ELSE null END),
    p.slug
)
FROM Product p
JOIN p.variants v ON v.isDefault = true
LEFT JOIN p.images img
WHERE p.status = 'ACTIVE'
GROUP BY p.id, p.name, p.category, v.price, p.slug
""")
    List<ProductCardResponse> findHomeProducts();



    @Query("""
SELECT new com.furniture.product.dto.ProductCardResponse(
    p.id,
    p.name,
    p.category,
    v.price,
    MAX(CASE WHEN img.imageType = com.furniture.product.enums.ImageType.PRIMARY THEN img.imageUrl END),
    MAX(CASE WHEN img.imageType = com.furniture.product.enums.ImageType.SECONDARY THEN img.imageUrl END),
    p.slug
)
FROM Product p
JOIN p.variants v ON v.isDefault = true
LEFT JOIN p.images img
WHERE p.status = 'ACTIVE'
GROUP BY p.id, p.name, p.category, v.price, p.slug
""")
    Page<ProductCardResponse> findPageableProduct(Pageable pageable);

}

