package com.furniture.product.controller;

import com.furniture.product.dto.ProductCardResponse;
import com.furniture.product.dto.ProductRequest;
import com.furniture.product.dto.ProductResponse;
import com.furniture.product.enums.ImageType;
import com.furniture.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
           @Valid @RequestBody ProductRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productService.createProduct(request));
    }

    @PostMapping(
            value = "/{productId}/images",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Void> uploadImages(
            @PathVariable Long productId,
            @RequestPart("files") List<MultipartFile> files,
            @RequestParam ImageType imageType
    ) {
        productService.addProductImages(productId, files, imageType);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping(value = "/{productId}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long productId){
        return ResponseEntity.ok(productService.getProduct(productId));
    }

    @PutMapping(value = "/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long productId,
                                                         @Valid @RequestBody ProductRequest productRequest){
        return ResponseEntity.ok(productService.updateProduct(productId, productRequest));
    }

    @GetMapping("/home")
    public ResponseEntity<List<ProductCardResponse>> getHomeProducts(){
        return ResponseEntity.ok(productService.getHomeProducts());
    }

    @DeleteMapping(value = "/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId){
        return ResponseEntity.noContent().build();
    }
}
