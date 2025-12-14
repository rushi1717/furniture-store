package com.furniture.product.service.impl;

import com.cloudinary.Cloudinary;
import com.furniture.product.entity.Product;
import com.furniture.product.entity.ProductImage;
import com.furniture.product.enums.ImageType;
import com.furniture.product.repository.ProductRepository;
import com.furniture.product.service.ImageUploadService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryImageUploadService implements ImageUploadService {

    private final Cloudinary cloudinary;

    @Override
    public String uploadImage(MultipartFile file) {
        try {
            Map<?, ?> result = cloudinary.uploader().upload(
                    file.getBytes(),
                    Map.of(
                            "folder", "furniture/products",
                            "resource_type", "image"
                    )
            );
            return result.get("secure_url").toString();
        } catch (Exception e) {
            throw new RuntimeException("Image upload failed", e);
        }
    }
}
