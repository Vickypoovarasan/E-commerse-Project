package com.example.ecommerce.model;

public class CartResponse {

    private Long cartId;
    private String productName;
    private double price;
    private int quantity;

    // Constructor
    public CartResponse(Long cartId, String productName, double price, int quantity) {
        this.cartId = cartId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }

    // Getters
    public Long getCartId() {
        return cartId;
    }

    public String getProductName() {
        return productName;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
}
