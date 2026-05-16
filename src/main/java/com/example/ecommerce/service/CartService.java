package com.example.ecommerce.service;

import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.CartResponse;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.CartRepository;
import com.example.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    // Add to cart
    public Cart addToCart(Cart cart) {
        return cartRepository.save(cart);
    }

    // Get cart by user

    public Cart updateQuantity(Long cartId, int quantity) {

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cart.setQuantity(quantity);

        return cartRepository.save(cart);
    }

    public List<CartResponse> getCartByUser(Long userId) {

        List<Cart> cartItems = cartRepository.findByUserId(userId);

        return cartItems.stream().map(item -> {

            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            return new CartResponse(
                    item.getCartId(),
                    product.getName(),
                    product.getPrice(),
                    item.getQuantity()
            );

        }).toList();
    }


    // Remove item
    public void removeFromCart(Long cartId) {
        if (!cartRepository.existsById(cartId)) {
            throw new ResourceNotFoundException("Cart item not found");
        }
        cartRepository.deleteById(cartId);
    }
}
