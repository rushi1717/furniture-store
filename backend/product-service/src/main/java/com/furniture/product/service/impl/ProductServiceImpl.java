package com.furniture.product.service.impl;

import com.furniture.product.dto.*;
import com.furniture.product.entity.*;
import com.furniture.product.enums.ImageType;
import com.furniture.product.exception.ProductNotFoundException;
import com.furniture.product.feign.InventoryClient;
import com.furniture.product.mapper.ProductMapper;
import com.furniture.product.repository.ProductRepository;
import com.furniture.product.service.ImageUploadService;
import com.furniture.product.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ImageUploadService imageUploadService;
    private final InventoryClient inventoryClient;

    @Override
    public ProductResponse createProduct(ProductRequest request) {

        Product product = new Product();
        product.setName(request.getName());
        product.setCategory(request.getCategory());
        product.setProductType(request.getProductType());
        product.setIsAvailable(request.getIsAvailable());
        product.setFeatures(request.getFeatures());
        product.setProducerEmail(request.getProducerEmail());
        product.setEan(request.getEan());
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
            variant.setFabricCode(vr.getFabricCode());
            variant.setColor(vr.getColor());
            variant.setDescription(vr.getDescription());
            variant.setIsDefault(vr.getIsDefault());
            variant.setVariantSlug(vr.getVariantSlug());
            variant.setProductWeightKg(vr.getProductWeightKg());
            variant.setMaxSupportedWeightKg(vr.getMaxSupportedWeightKg());
            variant.setTotalWeightWithPackagingKg(
                    vr.getTotalWeightWithPackagingKg()
            );

            variant.setProduct(product);
            product.getVariants().add(variant);

            ProductMaterial material = new ProductMaterial();
            material.setFabricName(vr.getMaterial().getFabricName());
            material.setDescription(vr.getMaterial().getDescription());
            material.setPetFriendly(vr.getMaterial().getPetFriendly());
            material.setScratchResistant(vr.getMaterial().getScratchResistant());
            material.setWarnings(vr.getMaterial().getWarnings());
            material.setVariant(variant);

            variant.setMaterial(material);

            ProductDimension dimension = new ProductDimension();
            dimension.setWidthCm(vr.getDimension().getWidthCm());
            dimension.setHeightCm(vr.getDimension().getHeightCm());
            dimension.setLengthCm(vr.getDimension().getLengthCm());
            dimension.setSeatHeightCm(vr.getDimension().getSeatHeightCm());
            dimension.setVariant(variant);
            variant.setDimension(dimension);

            for (PackageRequest pr : vr.getPackages()) {
                ProductPackage pack = new ProductPackage();
                pack.setBoxNumber(pr.getBoxNumber());
                pack.setWidthCm(pr.getWidthCm());
                pack.setHeightCm(pr.getHeightCm());
                pack.setLengthCm(pr.getLengthCm());
                pack.setWeightKg(pr.getWeightKg());
                pack.setVariant(variant);
                variant.getPackages().add(pack);
            }
        }

        Product savedProduct = productRepository.save(product);

        // 🔥 INVENTORY CREATION
        for (ProductVariant variant : savedProduct.getVariants()) {

            VariantRequest vr =
                    request.getVariants().stream()
                            .filter(v ->
                                    v.getVariantSlug()
                                            .equals(variant.getVariantSlug())
                            )
                            .findFirst()
                            .orElseThrow();

            Integer stock = vr.getInitialStock();
            if (stock == null || stock < 0) {
                stock = 0;
            }

            inventoryClient.createStock(
                    new CreateInventoryRequest(
                            savedProduct.getId(),
                            variant.getId(),
                            stock
                    )
            );
        }

        return productMapper.toResponse(savedProduct);
    }

    @Override
    public List<ProductCardResponse> findByIds(List<Long> ids) {

        return productRepository.findProductsWithDefaultVariant(ids)
                .stream()
                .map(product -> {

                    ProductVariant defaultVariant = product.getVariants()
                            .stream()
                            .filter(v -> Boolean.TRUE.equals(v.getIsDefault()))
                            .findFirst()
                            .orElseThrow(() ->
                                    new IllegalStateException("Default variant missing for product " + product.getId())
                            );

                    String primaryImageUrl = defaultVariant.getImages()
                            .stream()
                            .filter(img -> img.getImageType() == ImageType.PRIMARY)
                            .map(ProductImage::getImageUrl)
                            .findFirst()
                            .orElse(null);

                    String secondaryImageUrl = defaultVariant.getImages()
                            .stream()
                            .filter(img -> img.getImageType() == ImageType.SECONDARY)
                            .map(ProductImage::getImageUrl)
                            .findFirst()
                            .orElse(null);

                    return new ProductCardResponse(
                            product.getId(),
                            product.getName(),
                            product.getCategory(),
                            defaultVariant.getPrice(),
                            primaryImageUrl,
                            secondaryImageUrl,
                            product.getSlug(),
                            defaultVariant.getDescription(),
                            defaultVariant.getVariantSlug()
                    );
                })
                .toList();
    }

    @Override
    public void addVariantImages(
            Long productId,
            Long variantId,
            List<MultipartFile> files,
            ImageType imageType
    ) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        ProductVariant variant = product.getVariants()
                .stream()
                .filter(v -> v.getId().equals(variantId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Variant not found"));

        // 🔒 SINGLE IMAGE TYPES
        if (imageType == ImageType.PRIMARY
                || imageType == ImageType.SECONDARY
                || imageType == ImageType.DIMENSION
                || imageType == ImageType.VARIANT
        ) {

            // remove existing image of this type
            variant.getImages()
                    .removeIf(img -> img.getImageType() == imageType);

            MultipartFile file = files.get(0); // only one allowed
            String url = imageUploadService.uploadImage(file);

            ProductImage image = new ProductImage();
            image.setImageUrl(url);
            image.setImageType(imageType);
            image.setVariant(variant);

            variant.getImages().add(image);
        }

        // 🖼️ MULTIPLE GALLERY IMAGES
        else if (imageType == ImageType.GALLERY) {

            for (MultipartFile file : files) {
                String url = imageUploadService.uploadImage(file);

                boolean exists = variant.getImages().stream()
                        .anyMatch(img ->
                                img.getImageType() == ImageType.GALLERY
                                        && img.getImageUrl().equals(url)
                        );

                if (!exists) {
                    ProductImage image = new ProductImage();
                    image.setImageUrl(url);
                    image.setImageType(ImageType.GALLERY);
                    image.setVariant(variant);

                    variant.getImages().add(image);
                }
            }
        }

        productRepository.save(product);
    }


    @Transactional()
    public ProductInfoResponse getProductBySlug(String slug) {

        Product product = productRepository
                .findBySlug(slug)
                .orElseThrow(() ->
                        new ProductNotFoundException("Product not found")
                );

        return productMapper.toInfoResponse(product);
    }


    @Transactional
    public VariantResponse getVariantBySlug(
            String productSlug,
            String variantSlug) {

        Product product = productRepository
                .findBySlugWithVariants(productSlug)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        ProductVariant variant = product.getVariants()
                .stream()
                .filter(v -> v.getVariantSlug().equals(variantSlug))
                .findFirst()
                .orElseThrow(() ->
                        new ProductNotFoundException("Variant not found"));

        return productMapper.toVariantResponse(variant);
    }


    @Override
    public ProductResponse updateProduct(Long id, ProductRequest request) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

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

    @Override
    public Page<ProductCardResponse> getProductsForLoadMore(Pageable pageable) {
        return productRepository.findPageableProduct(pageable);
    }

    public ProductVariantResponse getVariant(Long productId, Long variantId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        ProductVariant variant = product.getVariants()
                .stream()
                .filter(v -> v.getId().equals(variantId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Variant not found"));

        ProductImage primaryImage = variant.getImages()
                .stream()
                .filter(img -> img.getImageType() == ImageType.PRIMARY)
                .findFirst()
                .orElse(null);


        ProductVariantResponse response = new ProductVariantResponse();
        response.setProductId(productId);
        response.setVariantId(variantId);
        response.setProductName(product.getName());
        response.setColor(variant.getColor());
        response.setSize(variant.getSize());
        response.setPrice(variant.getPrice());
        response.setCurrency(variant.getCurrency());
        response.setPrimaryImageUrl(
                primaryImage != null ? primaryImage.getImageUrl() : null
        );

        return response;
    }





    private void updateVariants(Product product, List<VariantRequest> requests) {

        Map<String, ProductVariant> existing =
                product.getVariants().stream()
                        .collect(Collectors.toMap(
                                ProductVariant::getVariantSlug,
                                Function.identity()
                        ));

        product.getVariants().clear();

        for (VariantRequest vr : requests) {

            ProductVariant variant =
                    existing.getOrDefault(vr.getVariantSlug(), new ProductVariant());

            variant.setProduct(product);
            variant.setDescription(vr.getDescription());
            variant.setSize(vr.getSize());
            variant.setPrice(vr.getPrice());
            variant.setCurrency(vr.getCurrency());
//            variant.setFabricName(vr.getFabricName());
            variant.setFabricCode(vr.getFabricCode());
            variant.setColor(vr.getColor());
            variant.setIsDefault(vr.getIsDefault());
            variant.setVariantSlug(vr.getVariantSlug());

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
