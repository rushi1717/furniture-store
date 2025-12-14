package com.furniture.product.mapper;

import com.furniture.product.dto.*;
import com.furniture.product.entity.Product;
import com.furniture.product.entity.ProductVariant;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductResponse toResponse(Product product) {

        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setCategory(product.getCategory());
        response.setShortDescription(product.getShortDescription());
        response.setLongDescription(product.getLongDescription());
        response.setReferenceNumber(product.getReferenceNumber());
        response.setHasStorage(product.getHasStorage());
        response.setDeliveryMinWeeks(product.getDeliveryMinWeeks());
        response.setDeliveryMaxWeeks(product.getDeliveryMaxWeeks());
        response.setSlug(product.getSlug());
        response.setStatus(product.getStatus());

        // ✅ Variants
        response.setVariants(
                product.getVariants().stream()
                        .map(this::mapVariant)
                        .toList()
        );

        // ✅ Images
        response.setImages(
                product.getImages().stream()
                        .map(img -> new ProductImageResponse(
                                img.getImageUrl(),
                                img.getImageType()))
                        .toList()
        );

        return response;
    }

    private VariantResponse mapVariant(ProductVariant variant) {

        VariantResponse vr = new VariantResponse();
        vr.setId(variant.getId());
        vr.setSize(variant.getSize());
        vr.setPrice(variant.getPrice());
        vr.setCurrency(variant.getCurrency());
        vr.setFabricName(variant.getFabricName());
        vr.setFabricCode(variant.getFabricCode());
        vr.setColor(variant.getColor());
        vr.setIsDefault(variant.getIsDefault());
        vr.setVariantSlug(variant.getVariantSlug());

        // ✅ Dimension
        if (variant.getDimension() != null) {
            DimensionResponse dr = new DimensionResponse();
            dr.setWidthCm(variant.getDimension().getWidthCm());
            dr.setHeightCm(variant.getDimension().getHeightCm());
            dr.setLengthCm(variant.getDimension().getLengthCm());
            dr.setSeatHeightCm(variant.getDimension().getSeatHeightCm());
            vr.setDimension(dr);
        }

        // ✅ Packages
        vr.setPackages(
                variant.getPackages().stream()
                        .map(pkg -> {
                            PackageResponse pr = new PackageResponse();
                            pr.setBoxNumber(pkg.getBoxNumber());
                            pr.setWidthCm(pkg.getWidthCm());
                            pr.setHeightCm(pkg.getHeightCm());
                            pr.setLengthCm(pkg.getLengthCm());
                            pr.setWeightKg(pkg.getWeightKg());
                            return pr;
                        })
                        .toList()
        );

        return vr;
    }
}
