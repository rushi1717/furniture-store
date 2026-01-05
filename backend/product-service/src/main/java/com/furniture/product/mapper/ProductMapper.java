package com.furniture.product.mapper;

import com.furniture.product.dto.*;
import com.furniture.product.entity.Product;
import com.furniture.product.entity.ProductImage;
import com.furniture.product.entity.ProductVariant;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductMapper {
    public ProductResponse toResponse(Product product) {

        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setProductType(product.getProductType());
        response.setCategory(product.getCategory());
        response.setShortDescription(product.getShortDescription());
        response.setLongDescription(product.getLongDescription());
        response.setReferenceNumber(product.getReferenceNumber());
        response.setHasStorage(product.getHasStorage());
        response.setIsAvailable(product.getIsAvailable());
        response.setDeliveryMinWeeks(product.getDeliveryMinWeeks());
        response.setDeliveryMaxWeeks(product.getDeliveryMaxWeeks());

        response.setSlug(product.getSlug());
        response.setStatus(product.getStatus());

        response.setFeatures(product.getFeatures());
        response.setProducerEmail(product.getProducerEmail());
        response.setEan(product.getEan());

        response.setVariants(
                product.getVariants()
                        .stream()
                        .map(this::mapVariant)
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
        vr.setDescription(variant.getDescription());
        vr.setFabricCode(variant.getFabricCode());
        vr.setColor(variant.getColor());
        vr.setIsDefault(variant.getIsDefault());
        vr.setVariantSlug(variant.getVariantSlug());

        // ✅ weights
        vr.setProductWeightKg(variant.getProductWeightKg());
        vr.setMaxSupportedWeightKg(variant.getMaxSupportedWeightKg());
        vr.setTotalWeightWithPackagingKg(
                variant.getTotalWeightWithPackagingKg()
        );

        // ✅ dimension
        if (variant.getDimension() != null) {
            DimensionResponse dr = new DimensionResponse();
            dr.setWidthCm(variant.getDimension().getWidthCm());
            dr.setHeightCm(variant.getDimension().getHeightCm());
            dr.setLengthCm(variant.getDimension().getLengthCm());
            dr.setSeatHeightCm(variant.getDimension().getSeatHeightCm());
            vr.setDimension(dr);
        }

        // ✅ packages
        vr.setPackages(
                variant.getPackages()
                        .stream()
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

        // ✅ material
        if (variant.getMaterial() != null) {
            MaterialResponse mr = new MaterialResponse();
            mr.setFabricName(variant.getMaterial().getFabricName());
            mr.setDescription(variant.getMaterial().getDescription());
            mr.setPetFriendly(variant.getMaterial().getPetFriendly());
            mr.setScratchResistant(variant.getMaterial().getScratchResistant());
            mr.setWarnings(variant.getMaterial().getWarnings());
            vr.setMaterial(mr);

            // keep fabricName in sync
            vr.setFabricName(variant.getMaterial().getFabricName());
        }

        // ✅ images + primary image
        // ✅ images (THIS is where it is used)
        vr.setImages(mapVariantImages(variant));

        return vr;
    }

    // ✅ PLACE THIS METHOD HERE
    private VariantImageResponse mapVariantImages(ProductVariant variant) {

        String primary = null;
        String secondary = null;
        String dimension = null;
        String variantImg = null;
        List<String> gallery = new ArrayList<>();

        for (ProductImage img : variant.getImages()) {
            switch (img.getImageType()) {
                case PRIMARY -> primary = img.getImageUrl();
                case SECONDARY -> secondary = img.getImageUrl();
                case DIMENSION -> dimension = img.getImageUrl();
                case VARIANT -> variantImg = img.getImageUrl();
                case GALLERY -> gallery.add(img.getImageUrl());
            }
        }

        return new VariantImageResponse(primary, secondary, dimension,variantImg, gallery);
    }

    public VariantResponse toVariantResponse(ProductVariant variant) {
        return mapVariant(variant); // reuse your existing mapping
    }


    public ProductInfoResponse toInfoResponse(Product product) {

        ProductInfoResponse r = new ProductInfoResponse();
        r.setId(product.getId());
        r.setName(product.getName());
        r.setProductType(product.getProductType());
        r.setCategory(product.getCategory());
        r.setShortDescription(product.getShortDescription());
        r.setLongDescription(product.getLongDescription());
        r.setReferenceNumber(product.getReferenceNumber());
        r.setHasStorage(product.getHasStorage());
        r.setIsAvailable(product.getIsAvailable());
        r.setDeliveryMinWeeks(product.getDeliveryMinWeeks());
        r.setDeliveryMaxWeeks(product.getDeliveryMaxWeeks());
        r.setSlug(product.getSlug());
        r.setStatus(product.getStatus());
        r.setFeatures(product.getFeatures());
        r.setProducerEmail(product.getProducerEmail());
        r.setEan(product.getEan());

        return r;
    }


}
