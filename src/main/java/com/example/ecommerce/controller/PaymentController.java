package com.example.ecommerce.controller;

import com.example.ecommerce.model.Payment;
import com.example.ecommerce.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

// Make Payment

    @PostMapping("/{orderId}")
    public Payment makePayment(@PathVariable Long orderId) {
        return paymentService.processPayment(orderId);
    }
}
