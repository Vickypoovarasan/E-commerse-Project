package com.example.ecommerce.model;

import java.util.List;

public class PaymentDetails {

    private Payment payment;
    private Order order;
    private User user;
    private List<OrderItem> orderItems;

    public PaymentDetails() {
    }

    public PaymentDetails(Payment payment, Order order, User user, List<OrderItem> orderItems) {
        this.payment = payment;
        this.order = order;
        this.user = user;
        this.orderItems = orderItems;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
