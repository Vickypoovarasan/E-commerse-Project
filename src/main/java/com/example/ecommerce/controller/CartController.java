package com.example.ecommerce.controller;

import com.example.ecommerce.model.ApiResponse;
import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.CartResponse;
import com.example.ecommerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {

    @Autowired
    private CartService cartService;

    // Add to cart
    @PostMapping
    public ResponseEntity<ApiResponse> addToCart(@RequestBody Cart cart) {
        Cart savedCart = cartService.addToCart(cart);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Item successfully added to cart", savedCart));
    }

    // Get cart items of user
    @GetMapping("/{userId}")
    public List<CartResponse> getCart(@PathVariable Long userId) {
        return cartService.getCartByUser(userId);
    }

    @PutMapping("/{cartId}")
    public ResponseEntity<ApiResponse> updateQuantity(@PathVariable Long cartId,
                                                      @RequestParam int quantity) {
        Cart updatedCart = cartService.updateQuantity(cartId, quantity);
        return ResponseEntity.ok(new ApiResponse("Cart quantity successfully updated", updatedCart));
    }

    // Remove item from cart
    @DeleteMapping("/{cartId}")
    public ResponseEntity<ApiResponse> removeItem(@PathVariable Long cartId) {
        cartService.removeFromCart(cartId);
        return ResponseEntity.ok(new ApiResponse("Item removed from cart"));
    }
}
