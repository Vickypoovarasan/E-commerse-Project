package com.example.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce.model.ApiResponse;
import com.example.ecommerce.model.PaymentDetails;
import com.example.ecommerce.service.PaymentService;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping(path = {"", "/", "/all"})
    public ResponseEntity<ApiResponse> getAllPayments() {
        List<PaymentDetails> payments = paymentService.getAllPaymentDetails();
        return ResponseEntity.ok(new ApiResponse("Payment history loaded", payments));
    }

    // Make Payment
    @PostMapping("/{orderId}")
    public ResponseEntity<ApiResponse> makePayment(@PathVariable Long orderId) {
        PaymentDetails paymentDetails = paymentService.processPayment(orderId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Payment processed successfully", paymentDetails));
    }
}
