package com.example.ecommerce.controller;

import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.CartResponse;
import com.example.ecommerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Cart addToCart(@RequestBody Cart cart) {
        return cartService.addToCart(cart);
    }

    // Get cart items of user
    @GetMapping("/{userId}")
    public List<CartResponse> getCart(@PathVariable Long userId) {
        return cartService.getCartByUser(userId);
    }


    @PutMapping("/{cartId}")
    public Cart updateQuantity(@PathVariable Long cartId, @RequestParam int quantity) {
        return cartService.updateQuantity(cartId, quantity);
    }



    // Remove item from cart
    @DeleteMapping("/{cartId}")
    public String removeItem(@PathVariable Long cartId) {
        cartService.removeFromCart(cartId);
        return "Item removed from cart";
    }
}
