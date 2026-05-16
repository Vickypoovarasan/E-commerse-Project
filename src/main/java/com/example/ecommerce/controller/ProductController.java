package com.example.ecommerce.controller;

import com.example.ecommerce.model.ApiResponse;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductService productService;

    // Bulk Add Products
    @PostMapping
    public ResponseEntity<ApiResponse> addProducts(@RequestBody List<Product> products) {
        List<Product> savedProducts = productService.addProducts(products);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Products successfully inserted", savedProducts));
    }

    // Get All Products
    @GetMapping
    public List<Product> getAllProducts(@RequestParam(value = "category", required = false) String category) {
        if (category != null && !category.isBlank()) {
            return productService.getProductsByCategory(category);
        }
        return productService.getAllProducts();
    }

    // Get Product by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getProduct(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(product -> ResponseEntity.ok(new ApiResponse("Product found", product)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("Product not found")));
    }

    // Update Product
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        Product updatedProduct = productService.updateProduct(id, product);
        return ResponseEntity.ok(new ApiResponse("Product successfully updated", updatedProduct));
    }

    // Delete Product
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(new ApiResponse("Product deleted successfully"));
    }
}