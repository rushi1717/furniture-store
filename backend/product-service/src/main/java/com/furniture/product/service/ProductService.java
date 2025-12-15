package com.furniture.product.service;

import com.furniture.product.dto.ProductCardResponse;
import com.furniture.product.dto.ProductRequest;
import com.furniture.product.dto.ProductResponse;
import com.furniture.product.enums.ImageType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    List<ProductResponse> getAllProducts();

    ProductResponse getProduct(Long productId);

    ProductResponse createProduct(ProductRequest request);

    void addProductImages(
            Long productId,
            List<MultipartFile> files,
            ImageType imageType
    );

    ProductResponse updateProduct(Long productId ,ProductRequest productRequest);

    List<ProductCardResponse> getHomeProducts();

    void deleteProduct(Long productId);

//    ProductCardResponse getProductsForLoadMore(Pageable pageable);
    Page<ProductCardResponse> getProductsForLoadMore(Pageable pageable);
}
