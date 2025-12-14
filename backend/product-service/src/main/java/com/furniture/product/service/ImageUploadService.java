package com.furniture.product.service;

import com.furniture.product.enums.ImageType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageUploadService {
    String uploadImage(MultipartFile file);

}
