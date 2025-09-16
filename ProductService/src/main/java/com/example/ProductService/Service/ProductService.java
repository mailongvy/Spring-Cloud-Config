package com.example.ProductService.Service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.example.ProductService.DTO.ProductDTO;
import com.example.ProductService.Exception.AlreadyExistsResource;
import com.example.ProductService.Exception.ResourceNotFound;
import com.example.ProductService.Model.Product;
import com.example.ProductService.Repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService implements IProductService {
    private final ProductRepository productRepository;

    private final ModelMapper modelMapper;

    @Override
    public Product getProductById(Long id) {
        Product product = productRepository.findById(id)
                                            .orElseThrow(() -> new ResourceNotFound("Product not found"));
                                
        return product;
    }

    @Override
    public Product addProduct(Product product) {
        if (productRepository.existsByName(product.getName())) {
            throw new AlreadyExistsResource(product.getName() + " already existed");
        }

        Product addProduct = createProduct(product);
        return productRepository.save(addProduct);


    }

    public Product createProduct(Product product) {
        return new Product(
            product.getName(),
            product.getPrice()
        );
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product updateProduct(Product product, Long id) {
        return productRepository.findById(id)
                                .map(existProduct -> {
                                    existProduct.setName(product.getName());
                                    existProduct.setPrice(product.getPrice());
                                    return productRepository.save(existProduct);
                                })
                                .orElseThrow(() -> new ResourceNotFound("Product Not Found"));
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.findById(id)
                        .ifPresentOrElse(
                            productRepository::delete, 
                            () -> {throw new ResourceNotFound("Product not found");}
                        );
    }

    @Override
    public ProductDTO convertToDto(Product product) {
        // TODO Auto-generated method stub
        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);

        return productDTO;
    }

    @Override
    public List<ProductDTO> getConvertedProducts(List<Product> products) {
        // TODO Auto-generated method stub
        return products.stream().map(this::convertToDto).toList();
    }

    
    

    
}
