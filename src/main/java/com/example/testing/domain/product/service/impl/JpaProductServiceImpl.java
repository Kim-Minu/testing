package com.example.testing.domain.product.service.impl;

import com.example.testing.domain.product.entity.Product;
import com.example.testing.domain.product.repository.ProductRepository;
import com.example.testing.domain.product.service.ProductService;
import com.example.testing.global.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JpaProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public JpaProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id " + id));
    }

    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }


}