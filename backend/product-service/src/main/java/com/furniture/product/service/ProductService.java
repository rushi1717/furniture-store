package com.furniture.product.service;

import com.furniture.product.dto.ProductCardResponse;
import com.furniture.product.dto.ProductRequest;
import com.furniture.product.dto.ProductResponse;
import com.furniture.product.enums.ImageType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    List<ProductResponse> getAllProducts();

    ProductResponse getProduct(Long id);

    ProductResponse createProduct(ProductRequest request);

//    void addProductImage(Long productId, MultipartFile file, ImageType imageType);

    void addProductImages(
            Long productId,
            List<MultipartFile> files,
            ImageType imageType
    );

    ProductResponse updateProduct(Long id ,ProductRequest productRequest);

    List<ProductCardResponse> getHomeProducts();

}
