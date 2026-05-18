package com.example.ecommerce.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ecommerce.exception.ResourceNotFoundException;
import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.OrderItem;
import com.example.ecommerce.model.Payment;
import com.example.ecommerce.model.PaymentDetails;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.OrderItemRepository;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.PaymentRepository;
import com.example.ecommerce.repository.UserRepository;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    // Process Payment
    public PaymentDetails processPayment(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        User user = userRepository.findById(order.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setAmount(order.getTotalAmount());
        payment.setPaymentStatus("SUCCESS"); // mock payment success
        payment.setPaymentDate(LocalDateTime.now());

        Payment savedPayment = paymentRepository.save(payment);
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);

        return new PaymentDetails(savedPayment, order, user, orderItems);
    }

    public List<PaymentDetails> getAllPaymentDetails() {
        List<Payment> payments = paymentRepository.findAll();
        return payments.stream().map(payment -> {
            Order order = orderRepository.findById(payment.getOrderId()).orElse(null);
            User user = order != null ? userRepository.findById(order.getUserId()).orElse(null) : null;
            List<OrderItem> orderItems = orderItemRepository.findByOrderId(payment.getOrderId());
            return new PaymentDetails(payment, order, user, orderItems);
        }).collect(Collectors.toList());
    }
}
