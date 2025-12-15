package com.furniture.product.controller;

import com.furniture.product.dto.ApiError;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<ApiError> handleError(HttpServletRequest request) {

        Integer statusCode = (Integer) request.getAttribute(
                RequestDispatcher.ERROR_STATUS_CODE
        );

        int status = statusCode != null ? statusCode : 500;

        ApiError error = new ApiError(
                status,
                status == 404 ? "API not found" : "Unexpected error",
                (String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI),
                LocalDateTime.now()
        );

        return ResponseEntity.status(status).body(error);
    }
}

