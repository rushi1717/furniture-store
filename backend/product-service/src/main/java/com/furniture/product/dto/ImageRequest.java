package com.furniture.product.dto;

import com.furniture.product.enums.ImageType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ImageRequest {

    @NotBlank
    private String imageUrl;   // Cloudinary secure URL

    @NotNull
    private ImageType imageType; // PRIMARY / SECONDARY / GALLERY

    // getters & setters
}
