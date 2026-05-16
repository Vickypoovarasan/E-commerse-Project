package com.example.ecommerce.controller;

import com.example.ecommerce.model.ApiResponse;
import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.OrderRequest;
import com.example.ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // Place Order
    @PostMapping("/{userId}")
    public ResponseEntity<ApiResponse> placeOrder(@PathVariable Long userId,
                                                  @RequestBody OrderRequest orderRequest) {
        Order savedOrder = orderService.placeOrder(userId, orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Order successfully placed", savedOrder));
    }

    // Get User Orders
    @GetMapping("/{userId}")
    public List<Order> getOrders(@PathVariable Long userId) {
        return orderService.getOrdersByUser(userId);
    }
}