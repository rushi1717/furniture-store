package com.furniture.product.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


public record ApiError(
        int status,
        String message,
        String path,
        LocalDateTime timestamp
) {}

