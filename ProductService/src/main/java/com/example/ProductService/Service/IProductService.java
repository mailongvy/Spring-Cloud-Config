package com.example.ProductService.Service;

import java.util.List;

import com.example.ProductService.DTO.ProductDTO;
import com.example.ProductService.Model.Product;

public interface IProductService {
    Product getProductById(Long id);

    Product addProduct(Product product);

    List<Product> getAllProducts();

    Product updateProduct(Product product, Long id);

    void deleteProduct(Long id);

    // convert to dto for product
    ProductDTO convertToDto(Product product);

    // convert to list of dto
    List<ProductDTO> getConvertedProducts(List<Product> products);

}
