# E-Commerce API Testing Guide

## Base URL
`http://localhost:9090`

---

## Overview
This guide covers all backend APIs for the ecommerce application, including authentication, products, cart, orders, payments, and invoices. Use these examples in Postman to verify the backend flow.

---

## 🔐 Authentication

### 1. Register User
Endpoint: `POST /api/users/register`

Request:
```http
POST {{base_url}}/api/users/register
Content-Type: application/json

{
  "username": "john_doe",
  "password": "securepass123",
  "email": "john@example.com",
  "role": "CUSTOMER"
}
```

Response:
```json
{
  "message": "User registered successfully",
  "data": {
    "userId": 1,
    "username": "john_doe",
    "password": "securepass123",
    "role": "CUSTOMER",
    "email": "john@example.com"
  }
}
```

### 2. Login User or Admin
Endpoint: `POST /api/users/login`

Notes:
- The `username` parameter may contain a username or an email address.
- Username, email, and password matching are case-sensitive.

Request by username:
```http
POST {{base_url}}/api/users/login?username=john_doe&password=securepass123
```

Request by email:
```http
POST {{base_url}}/api/users/login?username=john@example.com&password=securepass123
```

Response for regular user:
```json
{
  "message": "login successfully",
  "data": {
    "userId": 1,
    "username": "john_doe",
    "password": "securepass123",
    "role": "CUSTOMER",
    "email": "john@example.com"
  }
}
```

Response for admin user:
```json
{
  "message": "admin login successfully",
  "data": {
    "userId": 2,
    "username": "admin_user",
    "password": "adminPass123",
    "role": "ADMIN",
    "email": "admin@example.com"
  }
}
```

Common login failures:
- `username and password mismatch`
- `email and password mismatch`
- `invalid username or email`

### 3. Get User by ID
Endpoint: `GET /api/users/{id}`

Request:
```http
GET {{base_url}}/api/users/1
```

Response:
```json
{
  "message": "User found",
  "data": {
    "userId": 1,
    "username": "john_doe",
    "password": "securepass123",
    "role": "CUSTOMER",
    "email": "john@example.com"
  }
}
```

---

## 🛍️ Product Management

### 1. Get All Products
Endpoint: `GET /api/products`

Request:
```http
GET {{base_url}}/api/products
```

Response:
```json
[
  {
    "productId": 1,
    "name": "Gaming Laptop",
    "description": "High-end gaming laptop with RTX 4090",
    "price": 1299.99,
    "stockQuantity": 5,
    "category": null
  },
  {
    "productId": 2,
    "name": "Wireless Mouse",
    "description": "Ergonomic wireless mouse",
    "price": 49.99,
    "stockQuantity": 20,
    "category": null
  }
]
```

### 2. Get Products by Category
Endpoint: `GET /api/products?category={category}`

Request:
```http
GET {{base_url}}/api/products?category=Electronics
```

Response:
```json
[
  {
    "productId": 1,
    "name": "Gaming Laptop",
    "description": "High-end gaming laptop",
    "price": 1299.99,
    "stockQuantity": 5,
    "category": "Electronics"
  }
]
```

### 3. Get Product by ID
Endpoint: `GET /api/products/{id}`

Request:
```http
GET {{base_url}}/api/products/1
```

Response:
```json
{
  "message": "Product found",
  "data": {
    "productId": 1,
    "name": "Gaming Laptop",
    "description": "High-end gaming laptop with RTX 4090",
    "price": 1299.99,
    "stockQuantity": 5,
    "category": null
  }
}
```

### 4. Add Products (Bulk)
Endpoint: `POST /api/products`

Request:
```http
POST {{base_url}}/api/products
Content-Type: application/json

[
  {
    "name": "Gaming Laptop",
    "description": "High-end gaming laptop",
    "price": 1299.99,
    "stockQuantity": 5,
    "category": "Electronics"
  },
  {
    "name": "Wireless Mouse",
    "description": "Ergonomic mouse",
    "price": 49.99,
    "stockQuantity": 20,
    "category": "Electronics"
  }
]
```

Response:
```json
{
  "message": "Products successfully inserted",
  "data": [
    {
      "productId": 1,
      "name": "Gaming Laptop",
      "description": "High-end gaming laptop",
      "price": 1299.99,
      "stockQuantity": 5,
      "category": "Electronics"
    },
    {
      "productId": 2,
      "name": "Wireless Mouse",
      "description": "Ergonomic mouse",
      "price": 49.99,
      "stockQuantity": 20,
      "category": "Electronics"
    }
  ]
}
```

### 5. Update Product
Endpoint: `PUT /api/products/{id}`

Request:
```http
PUT {{base_url}}/api/products/1
Content-Type: application/json

{
  "name": "Gaming Laptop Pro",
  "description": "Ultimate gaming laptop with RTX 4090",
  "price": 1499.99,
  "stockQuantity": 3,
  "category": "Electronics"
}
```

Response:
```json
{
  "message": "Product successfully updated",
  "data": {
    "productId": 1,
    "name": "Gaming Laptop Pro",
    "description": "Ultimate gaming laptop with RTX 4090",
    "price": 1499.99,
    "stockQuantity": 3,
    "category": "Electronics"
  }
}
```

### 6. Delete Product
Endpoint: `DELETE /api/products/{id}`

Request:
```http
DELETE {{base_url}}/api/products/3
```

Response:
```json
{
  "message": "Product deleted successfully"
}
```

---

## 🛒 Shopping Cart

### 1. Add Item to Cart
Endpoint: `POST /api/cart`

Request:
```http
POST {{base_url}}/api/cart
Content-Type: application/json

{
  "userId": 1,
  "productId": 1,
  "quantity": 1
}
```

Response:
```json
{
  "message": "Item successfully added to cart",
  "data": {
    "cartId": 1,
    "userId": 1,
    "productId": 1,
    "quantity": 1
  }
}
```

### 2. View User Cart
Endpoint: `GET /api/cart/{userId}`

Request:
```http
GET {{base_url}}/api/cart/1
```

Response:
```json
[
  {
    "cartId": 1,
    "productName": "Gaming Laptop Pro",
    "price": 1499.99,
    "quantity": 1
  },
  {
    "cartId": 2,
    "productName": "Wireless Mouse",
    "price": 49.99,
    "quantity": 2
  }
]
```

### 3. Update Cart Item Quantity
Endpoint: `PUT /api/cart/{cartId}?quantity={value}`

Request:
```http
PUT {{base_url}}/api/cart/1?quantity=3
```

Response:
```json
{
  "message": "Cart quantity successfully updated",
  "data": {
    "cartId": 1,
    "userId": 1,
    "productId": 1,
    "quantity": 3
  }
}
```

### 4. Remove Item from Cart
Endpoint: `DELETE /api/cart/{cartId}`

Request:
```http
DELETE {{base_url}}/api/cart/1
```

Response:
```json
{
  "message": "Item removed from cart"
}
```

---

## 📦 Order Management

### 1. Place Order
Endpoint: `POST /api/orders/{userId}`

Request:
```http
POST {{base_url}}/api/orders/1
Content-Type: application/json

{
  "shippingAddress": "123 Main Street, City",
  "deliveryNote": "Leave at the front desk"
}
```

Response:
```json
{
  "message": "Order successfully placed",
  "data": {
    "orderId": 1,
    "userId": 1,
    "totalAmount": 2549.97,
    "orderDate": "2026-05-13T08:30:00",
    "status": "PLACED",
    "shippingAddress": "123 Main Street, City",
    "deliveryNote": "Leave at the front desk"
  }
}
```

### 2. Get User Orders
Endpoint: `GET /api/orders/{userId}`

Request:
```http
GET {{base_url}}/api/orders/1
```

Response:
```json
[
  {
    "orderId": 1,
    "userId": 1,
    "totalAmount": 2549.97,
    "orderDate": "2026-05-13T08:30:00",
    "status": "PLACED",
    "shippingAddress": "123 Main Street, City",
    "deliveryNote": "Leave at the front desk"
  }
]
```

---

## 💳 Payment Processing

### 1. Process Payment
Endpoint: `POST /api/payments/{orderId}`

Request:
```http
POST {{base_url}}/api/payments/1
```

Response:
```json
{
  "message": "Payment processed successfully",
  "data": {
    "paymentId": 1,
    "orderId": 1,
    "amount": 2549.97,
    "paymentStatus": "SUCCESS",
    "paymentDate": "2026-05-13T08:31:00"
  }
}
```

---

## 🧾 Invoice Generation

### 1. Download Invoice PDF
Endpoint: `GET /api/invoices/{orderId}`

Request:
```http
GET {{base_url}}/api/invoices/1
```

Response:
- `200 OK`
- `Content-Type: application/pdf`
- Downloaded file: `invoice-1.pdf`

### 2. Download Invoice Text
Endpoint: `GET /api/invoices/{orderId}/txt`

Request:
```http
GET {{base_url}}/api/invoices/1/txt
```

Response:
- `200 OK`
- `Content-Type: text/plain`
- Downloaded file: `invoice-1.txt`

---

## ✅ End-to-end Test Flow

1. Register a customer
2. Login as customer using username or email
3. Add products as admin
4. Add product items to the customer cart
5. View cart contents
6. Update cart quantity if needed
7. Place order for the customer
8. Process payment for the order
9. Download invoice after order creation
10. View customer orders

---

## 🚨 Error Cases

### Login with wrong password
Request:
```http
POST {{base_url}}/api/users/login?username=john_doe&password=wrongpass
```
Response:
```json
{
  "timestamp": "...",
  "status": 400,
  "error": "Bad Request",
  "message": "username and password mismatch",
  "path": "/api/users/login"
}
```

### Login with wrong email
Request:
```http
POST {{base_url}}/api/users/login?username=john@example.com&password=wrongpass
```
Response:
```json
{
  "timestamp": "...",
  "status": 400,
  "error": "Bad Request",
  "message": "email and password mismatch",
  "path": "/api/users/login"
}
```

### Invalid product update
Request:
```http
PUT {{base_url}}/api/products/999
Content-Type: application/json

{
  "name": "Missing Product",
  "description": "Test",
  "price": 9.99,
  "stockQuantity": 1
}
```
Response:
```json
{
  "timestamp": "...",
  "status": 404,
  "error": "Not Found",
  "message": "Product not found",
  "path": "/api/products/999"
}
```

### Missing cart item
Request:
```http
DELETE {{base_url}}/api/cart/999
```
Response:
```json
{
  "timestamp": "...",
  "status": 404,
  "error": "Not Found",
  "message": "Cart item not found",
  "path": "/api/cart/999"
}
```

### Missing order for payment
Request:
```http
POST {{base_url}}/api/payments/999
```
Response:
```json
{
  "timestamp": "...",
  "status": 404,
  "error": "Not Found",
  "message": "Order not found",
  "path": "/api/payments/999"
}
```

---

## 📌 Postman Setup

- Create environment variable `base_url` = `http://localhost:9090`
- Use `{{base_url}}` in request URLs
- Set `Content-Type: application/json` for POST/PUT requests
- Save response values to environment variables for chained requests

---

## 🔢 API Summary

| Endpoint | Method | Description |
|---|---|---|
| `/api/users/register` | POST | Register user or admin |
| `/api/users/login` | POST | Login by username or email |
| `/api/users/{id}` | GET | Get user by id |
| `/api/products` | GET | List products |
| `/api/products?category=...` | GET | Filter products by category |
| `/api/products` | POST | Bulk insert products |
| `/api/products/{id}` | GET | Get product by id |
| `/api/products/{id}` | PUT | Update product |
| `/api/products/{id}` | DELETE | Delete product |
| `/api/cart` | POST | Add item to cart |
| `/api/cart/{userId}` | GET | View cart by user |
| `/api/cart/{cartId}` | PUT | Update cart quantity |
| `/api/cart/{cartId}` | DELETE | Remove cart item |
| `/api/orders/{userId}` | POST | Place order |
| `/api/orders/{userId}` | GET | Get user orders |
| `/api/payments/{orderId}` | POST | Process payment |
| `/api/invoices/{orderId}` | GET | Download invoice PDF |
| `/api/invoices/{orderId}/txt` | GET | Download invoice text |

---

## ✅ Notes

- The login flow accepts `username` as either username or email.
- Admin login returns `admin login successfully`.
- Most POST/PUT/DELETE responses are wrapped in `message` and `data`.
- GET endpoints may return raw arrays for cart and orders.
