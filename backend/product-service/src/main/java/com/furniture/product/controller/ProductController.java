package com.furniture.product.controller;

import com.furniture.product.dto.*;
import com.furniture.product.enums.ImageType;
import com.furniture.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // =========================================================
    // 🔓 PUBLIC ROUTES (NO AUTH REQUIRED)
    // Used by: website visitors, homepage, product listing
    // =========================================================

    @GetMapping("/slug/{slug}")
    public ResponseEntity<ProductInfoResponse> getBySlug(
            @PathVariable String slug) {

        return ResponseEntity.ok(
                productService.getProductBySlug(slug)
        );
    }

    @GetMapping("/slug/{productSlug}/variant/{variantSlug}")
    public ResponseEntity<VariantResponse> getVariantBySlug(
            @PathVariable String productSlug,
            @PathVariable String variantSlug) {

        return ResponseEntity.ok(
                productService.getVariantBySlug(productSlug, variantSlug)
        );
    }


    @GetMapping
    public ResponseEntity<List<ProductResponse>> getProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @PostMapping("/by-ids")
    public List<ProductCardResponse> getProductsByIds(@RequestBody List<Long> ids) {
        return productService.findByIds(ids);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProduct(
            @PathVariable Long productId) {

        return ResponseEntity.ok(productService.getProduct(productId));
    }

    @GetMapping("/{productId}/variants/{variantId}")
    public ResponseEntity<ProductVariantResponse> getVariant(
            @PathVariable Long productId,
            @PathVariable Long variantId) {

        return ResponseEntity.ok(
                productService.getVariant(productId, variantId)
        );
    }

    @GetMapping("/home")
    public ResponseEntity<List<ProductCardResponse>> getHomeProducts() {
        return ResponseEntity.ok(productService.getHomeProducts());
    }

    @GetMapping("/load-more")
    public ResponseEntity<Page<ProductCardResponse>> loadMoreProducts(
            @RequestParam(defaultValue = "0") int page) {

        int SIZE = 26;
        Pageable pageable = PageRequest.of(page, SIZE);

        return ResponseEntity.ok(
                productService.getProductsForLoadMore(pageable)
        );
    }

    // =========================================================
    // 🔒 AUTHENTICATED / ADMIN ROUTES
    // Protected by API Gateway (ADMIN role)
    // Used by: admin panel, product management
    // =========================================================

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody ProductRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productService.createProduct(request));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long productId,
            @Valid @RequestBody ProductRequest productRequest) {

        return ResponseEntity.ok(
                productService.updateProduct(productId, productRequest)
        );
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long productId) {

        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(
            value = "/{productId}/variants/{variantId}/images",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Void> uploadVariantImages(
            @PathVariable Long productId,
            @PathVariable Long variantId,
            @RequestPart("files") List<MultipartFile> files,
            @RequestParam ImageType imageType
    ) {
        productService.addVariantImages(productId, variantId, files, imageType);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
