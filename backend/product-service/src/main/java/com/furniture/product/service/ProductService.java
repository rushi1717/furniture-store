package com.furniture.product.service;

import com.furniture.product.dto.*;
import com.furniture.product.enums.ImageType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    List<ProductResponse> getAllProducts();

    ProductResponse getProduct(Long productId);

    ProductResponse createProduct(ProductRequest request);

    List<ProductCardResponse> findByIds(List<Long> ids);

    ProductInfoResponse getProductBySlug(String slug);

    VariantResponse getVariantBySlug(String productSlug, String variantSlug);

    ProductResponse updateProduct(Long productId ,ProductRequest productRequest);

    List<ProductCardResponse> getHomeProducts();

    void deleteProduct(Long productId);

    void addVariantImages(
            Long productId,
            Long variantId,
            List<MultipartFile> files,
            ImageType imageType
    );

    ProductVariantResponse getVariant(Long productId, Long variantId);
//    ProductCardResponse getProductsForLoadMore(Pageable pageable);
    Page<ProductCardResponse> getProductsForLoadMore(Pageable pageable);
}
