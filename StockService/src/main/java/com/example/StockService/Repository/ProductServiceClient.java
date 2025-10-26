package com.example.StockService.Repository;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.StockService.dto.ApiResponse;

@FeignClient(name = "ProductService", configuration = FeignClientConfiguration.class)
public interface ProductServiceClient {
    @GetMapping("/product/{id}")
    ApiResponse getProductById(@PathVariable("id") Long id);

}
