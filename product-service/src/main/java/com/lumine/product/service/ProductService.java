package com.lumine.product.service;

import com.lumine.product.dto.ProductRequest;
import com.lumine.product.dto.ProductResponse;
import com.lumine.product.model.Product;
import com.lumine.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public void createProduct(ProductRequest productRequest){
        // Builds immutable product from request data
        Product product = Product.builder()
                .name(productRequest.name())
                .description(productRequest.description())
                .price(productRequest.price())
                .build();

        productRepository.save(product);
        log.info("Product {} is saved", product.getId());
    }

    public List<ProductResponse> getAllProducts(){
        List<Product> products = productRepository.findAll();

       return products.stream()
               .map(this::mapToProductResponse)
               .toList();
    }

    public ProductResponse getProductById(String id){
        return productRepository.findById(id)
                .map(this::mapToProductResponse)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    private ProductResponse mapToProductResponse(Product product){
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice()
        );

    }

}
