package com.example.ecommerce.service;

import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // Bulk Add Products
    public List<Product> addProducts(List<Product> products) {
        return productRepository.saveAll(products);
    }


    // Get All Products
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Get products by category
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    // Get Product by ID
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    // Update Product
    public Product updateProduct(Long id, Product newProduct) {

        return productRepository.findById(id).map(product -> {
            product.setName(newProduct.getName());
            product.setDescription(newProduct.getDescription());
            product.setPrice(newProduct.getPrice());
            product.setStockQuantity(newProduct.getStockQuantity());
            product.setCategory(newProduct.getCategory());
            return productRepository.save(product);
        }).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    // Delete Product
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found");
        }
        productRepository.deleteById(id);
    }
}
