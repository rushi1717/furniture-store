package com.furniture.product.service.impl;

import com.furniture.product.dto.*;
import com.furniture.product.entity.*;
import com.furniture.product.enums.ImageType;
import com.furniture.product.exception.ProductNotFoundException;
import com.furniture.product.mapper.ProductMapper;
import com.furniture.product.repository.ProductRepository;
import com.furniture.product.service.ImageUploadService;
import com.furniture.product.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ImageUploadService imageUploadService;

    @Override
    public ProductResponse createProduct(ProductRequest request) {

        Product product = new Product();
        product.setName(request.getName());
        product.setCategory(request.getCategory());
        product.setShortDescription(request.getShortDescription());
        product.setLongDescription(request.getLongDescription());
        product.setReferenceNumber(request.getReferenceNumber());
        product.setHasStorage(request.getHasStorage());
        product.setDeliveryMinWeeks(request.getDeliveryMinWeeks());
        product.setDeliveryMaxWeeks(request.getDeliveryMaxWeeks());
        product.setSlug(request.getSlug());
        product.setStatus("ACTIVE");

        for (VariantRequest vr : request.getVariants()) {
            ProductVariant variant = new ProductVariant();
            variant.setSize(vr.getSize());
            variant.setPrice(vr.getPrice());
            variant.setCurrency(vr.getCurrency());
            variant.setFabricName(vr.getFabricName());
            variant.setFabricCode(vr.getFabricCode());
            variant.setColor(vr.getColor());
            variant.setIsDefault(vr.getIsDefault());
            variant.setVariantSlug(vr.getVariantSlug());

            variant.setProduct(product);
            product.getVariants().add(variant);

            /* ---------- DIMENSION ---------- */
            DimensionRequest dr = vr.getDimension();

            ProductDimension dimension = new ProductDimension();
            dimension.setWidthCm(dr.getWidthCm());
            dimension.setHeightCm(dr.getHeightCm());
            dimension.setLengthCm(dr.getLengthCm());
            dimension.setSeatHeightCm(dr.getSeatHeightCm());

            // bidirectional link
            dimension.setVariant(variant);
            variant.setDimension(dimension);

            for (PackageRequest pr : vr.getPackages()) {

                ProductPackage pack = new ProductPackage();
                pack.setBoxNumber(pr.getBoxNumber());
                pack.setWidthCm(pr.getWidthCm());
                pack.setHeightCm(pr.getHeightCm());
                pack.setLengthCm(pr.getLengthCm());
                pack.setWeightKg(pr.getWeightKg());

                // bidirectional link
                pack.setVariant(variant);
                variant.getPackages().add(pack);
            }
        }

        return productMapper.toResponse(productRepository.save(product));
    }

    @Override
    public void addProductImages(
            Long productId,
            List<MultipartFile> files,
            ImageType imageType
    ) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        for (MultipartFile file : files) {

            String url = imageUploadService.uploadImage(file);

            // ✅ DUPLICATE CHECK — PUT IT HERE
            boolean exists = product.getImages().stream()
                    .anyMatch(img ->
                            img.getImageUrl().equals(url)
                                    && img.getImageType() == imageType
                    );

            if (!exists) {
                ProductImage image = new ProductImage();
                image.setImageUrl(url);
                image.setImageType(imageType);
                image.setProduct(product);

                product.getImages().add(image);
            }
        }

        productRepository.save(product);
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest request) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        // 🔹 update basic fields
        product.setName(request.getName());
        product.setCategory(request.getCategory());
        product.setShortDescription(request.getShortDescription());
        product.setLongDescription(request.getLongDescription());
        product.setReferenceNumber(request.getReferenceNumber());
        product.setHasStorage(request.getHasStorage());
        product.setDeliveryMinWeeks(request.getDeliveryMinWeeks());
        product.setDeliveryMaxWeeks(request.getDeliveryMaxWeeks());
        product.setSlug(request.getSlug());

        // 🔹 update variants
        updateVariants(product, request.getVariants());

        // JPA dirty checking will persist changes
        return productMapper.toResponse(product);
    }

    @Override
    public List<ProductCardResponse> getHomeProducts() {
        return productRepository.findHomeProducts();
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
        productRepository.delete(product);
        log.info("Deleting product with id {}", id);
    }


    private void updateVariants(Product product, List<VariantRequest> variants) {

        // remove existing variants
        product.getVariants().clear();

        for (VariantRequest vr : variants) {

            ProductVariant variant = new ProductVariant();
            variant.setProduct(product);
            variant.setSize(vr.getSize());
            variant.setPrice(vr.getPrice());
            variant.setCurrency(vr.getCurrency());
            variant.setFabricName(vr.getFabricName());
            variant.setFabricCode(vr.getFabricCode());
            variant.setColor(vr.getColor());
            variant.setIsDefault(vr.getIsDefault());
            variant.setVariantSlug(vr.getVariantSlug());

            // ✅ dimension
            if (vr.getDimension() != null) {
                ProductDimension d = new ProductDimension();
                d.setWidthCm(vr.getDimension().getWidthCm());
                d.setHeightCm(vr.getDimension().getHeightCm());
                d.setLengthCm(vr.getDimension().getLengthCm());
                d.setSeatHeightCm(vr.getDimension().getSeatHeightCm());
                d.setVariant(variant); // IMPORTANT
                variant.setDimension(d);
            }

            // ✅ packages (Set-safe update)
            if (vr.getPackages() != null) {
                variant.getPackages().clear();

                for (PackageRequest p : vr.getPackages()) {
                    ProductPackage pkg = new ProductPackage();
                    pkg.setVariant(variant); // IMPORTANT
                    pkg.setBoxNumber(p.getBoxNumber());
                    pkg.setWidthCm(p.getWidthCm());
                    pkg.setHeightCm(p.getHeightCm());
                    pkg.setLengthCm(p.getLengthCm());
                    pkg.setWeightKg(p.getWeightKg());

                    variant.getPackages().add(pkg);
                }
            }

            product.getVariants().add(variant);
        }
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAllWithDetails()
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }

    @Override
    public ProductResponse getProduct(Long id) {
        return productMapper.toResponse(
                productRepository.findById(id)
                        .orElseThrow(() -> new ProductNotFoundException("Not found"))
        );
    }
}
