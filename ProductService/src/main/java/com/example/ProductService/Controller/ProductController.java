package com.example.ProductService.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ProductService.DTO.ProductDTO;
import com.example.ProductService.Model.Product;
import com.example.ProductService.Response.ApiResponse;
import com.example.ProductService.Service.IProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RefreshScope
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final IProductService productService;

    @Value("${app.name}")
    private String appName;

    @Value("${app.environment}")
    private String environment;

    @Value("${api.discount.rate}")
    private double discountRate;

    @GetMapping("/config")
    public String getConfig() {
        return String.format("App: %s<br>Environment: %s<br>Discount Rate: %.2f", 
                             appName, environment, discountRate);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long id) {
        try {
            Product product = productService.getProductById(id);
            ProductDTO productDTO = productService.convertToDto(product);
            return ResponseEntity.ok(new ApiResponse("Product found", productDTO));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(new ApiResponse("Product Not Found", null));
        }

    }

    @GetMapping("/getAll")
    public ResponseEntity<ApiResponse> getAllProduct() {
        try {
            List<Product> list = productService.getAllProducts();
            List<ProductDTO> listDTO = productService.getConvertedProducts(list);
            return ResponseEntity.ok(new ApiResponse("Product found", listDTO));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(new ApiResponse("Product Not Found", null));
        }

    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody Product product) {
        try {
            Product addProduct = productService.addProduct(product);
            return ResponseEntity.ok(new ApiResponse("Add Product Sucessfully", addProduct));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return ResponseEntity.status(HttpStatus.CONFLICT)
                                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody Product product, @PathVariable Long id) {
        try {
            Product updateProduct = productService.updateProduct(product, id);
            return ResponseEntity.ok(new ApiResponse("Update successfully", updateProduct));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                    .body(new ApiResponse(e.getMessage(), null));
        }


    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok(new ApiResponse("Product deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(new ApiResponse(e.getMessage(), null));
        }
    }





}
