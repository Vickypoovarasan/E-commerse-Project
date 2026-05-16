package com.example.ecommerce.controller;

import com.example.ecommerce.model.ApiResponse;
import com.example.ecommerce.model.Payment;
import com.example.ecommerce.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    // Make Payment
    @PostMapping("/{orderId}")
    public ResponseEntity<ApiResponse> makePayment(@PathVariable Long orderId) {
        Payment payment = paymentService.processPayment(orderId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Payment processed successfully", payment));
    }
}
