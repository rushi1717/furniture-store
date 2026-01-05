package com.furniture.product.repository;

import com.furniture.product.dto.ProductCardResponse;
import com.furniture.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    @Query("""
    SELECT DISTINCT p FROM Product p
    LEFT JOIN FETCH p.variants v
    LEFT JOIN FETCH v.images
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
    MAX(CASE WHEN img.imageType = com.furniture.product.enums.ImageType.PRIMARY THEN img.imageUrl END),
    MAX(CASE WHEN img.imageType = com.furniture.product.enums.ImageType.SECONDARY THEN img.imageUrl END),
    p.slug,
    v.description,
    v.variantSlug
)
FROM Product p
JOIN p.variants v ON v.isDefault = true
LEFT JOIN v.images img
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
    p.slug,
    v.description,
    v.variantSlug
)
FROM Product p
JOIN p.variants v ON v.isDefault = true
LEFT JOIN v.images img
WHERE p.status = 'ACTIVE'
GROUP BY p.id, p.name, p.category, v.price, p.slug
""")
    Page<ProductCardResponse> findPageableProduct(Pageable pageable);


    @Query("""
        SELECT DISTINCT p
        FROM Product p
        JOIN FETCH p.variants v
        WHERE p.status = 'ACTIVE'
          AND v.isDefault = true
          AND p.id IN :ids
    """)
    List<Product> findProductsWithDefaultVariant(@Param("ids") List<Long> ids);


//    @Query("""
//    SELECT p FROM Product p
//    LEFT JOIN FETCH p.variants v
//    LEFT JOIN FETCH v.images
//    LEFT JOIN FETCH v.dimension
//    LEFT JOIN FETCH v.material
//    WHERE p.slug = :slug
//""")
//    Optional<Product> findBySlugWithDetails(@Param("slug") String slug);
//
    Optional<Product> findBySlug(String slug);


    @Query("""
    SELECT p FROM Product p
    LEFT JOIN FETCH p.variants v
    LEFT JOIN FETCH v.images
    LEFT JOIN FETCH v.dimension
    LEFT JOIN FETCH v.material
    WHERE p.slug = :slug
""")
    Optional<Product> findBySlugWithVariants(@Param("slug") String slug);
}


