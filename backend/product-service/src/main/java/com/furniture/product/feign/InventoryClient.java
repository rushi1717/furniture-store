package com.furniture.product.feign;

import com.furniture.product.dto.CreateInventoryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "inventory-service",url = "http://localhost:8084")
public interface InventoryClient {

    @PostMapping("/api/v1/inventory/create")
    void createStock(@RequestBody CreateInventoryRequest request);
}
