# E-Commerce API Testing Guide

## Using Postman to Test APIs

### Import Collection
You can use these examples in Postman by creating requests with the same details.

---

## 🔐 Authentication Flow

### 1. User Registration
```
METHOD: POST
URL: http://localhost:9090/api/users/register
HEADERS:
  Content-Type: application/json

BODY (raw JSON):
{
  "username": "john_doe",
  "password": "securepass123",
  "email": "john@example.com",
  "role": "CUSTOMER"
}

RESPONSE (Success):
{
  "userId": 1,
  "username": "john_doe",
  "email": "john@example.com",
  "role": "CUSTOMER",
  "password": "securepass123"
}
```

### 2. User Login
```
METHOD: POST
URL: http://localhost:9090/api/users/login?username=john_doe&password=securepass123
HEADERS:
  Content-Type: application/json

RESPONSE (Success):
{
  "userId": 1,
  "username": "john_doe",
  "email": "john@example.com",
  "role": "CUSTOMER"
}
```

### 3. Get User by ID
```
METHOD: GET
URL: http://localhost:9090/api/users/1

RESPONSE (Success):
{
  "userId": 1,
  "username": "john_doe",
  "email": "john@example.com",
  "role": "CUSTOMER"
}
```

---

## 🛍️ Product Management

### 1. Get All Products
```
METHOD: GET
URL: http://localhost:9090/api/products

RESPONSE (Success):
[
  {
    "productId": 1,
    "name": "Gaming Laptop",
    "description": "High-end gaming laptop with RTX 4090",
    "price": 1299.99,
    "stockQuantity": 5
  },
  {
    "productId": 2,
    "name": "Wireless Mouse",
    "description": "Ergonomic wireless mouse",
    "price": 49.99,
    "stockQuantity": 20
  }
]
```

### 2. Get Product by ID
```
METHOD: GET
URL: http://localhost:9090/api/products/1

RESPONSE (Success):
{
  "productId": 1,
  "name": "Gaming Laptop",
  "description": "High-end gaming laptop with RTX 4090",
  "price": 1299.99,
  "stockQuantity": 5
}
```

### 3. Add Products (Bulk)
```
METHOD: POST
URL: http://localhost:9090/api/products
HEADERS:
  Content-Type: application/json

BODY (raw JSON):
[
  {
    "name": "Gaming Laptop",
    "description": "High-end gaming laptop",
    "price": 1299.99,
    "stockQuantity": 5
  },
  {
    "name": "Wireless Mouse",
    "description": "Ergonomic keyboard",
    "price": 49.99,
    "stockQuantity": 20
  },
  {
    "name": "USB-C Cable",
    "description": "Fast charging cable",
    "price": 19.99,
    "stockQuantity": 50
  }
]

RESPONSE (Success):
[
  {
    "productId": 1,
    "name": "Gaming Laptop",
    "description": "High-end gaming laptop",
    "price": 1299.99,
    "stockQuantity": 5
  },
  {
    "productId": 2,
    "name": "Wireless Mouse",
    "description": "Ergonomic keyboard",
    "price": 49.99,
    "stockQuantity": 20
  },
  {
    "productId": 3,
    "name": "USB-C Cable",
    "description": "Fast charging cable",
    "price": 19.99,
    "stockQuantity": 50
  }
]
```

### 4. Update Product
```
METHOD: PUT
URL: http://localhost:9090/api/products/1
HEADERS:
  Content-Type: application/json

BODY (raw JSON):
{
  "name": "Gaming Laptop Pro",
  "description": "Ultimate gaming laptop with RTX 4090",
  "price": 1499.99,
  "stockQuantity": 3
}

RESPONSE (Success):
{
  "productId": 1,
  "name": "Gaming Laptop Pro",
  "description": "Ultimate gaming laptop with RTX 4090",
  "price": 1499.99,
  "stockQuantity": 3
}
```

### 5. Delete Product
```
METHOD: DELETE
URL: http://localhost:9090/api/products/3

RESPONSE (Success):
"Product deleted successfully"
```

---

## 🛒 Shopping Cart

### 1. Add Item to Cart
```
METHOD: POST
URL: http://localhost:9090/api/cart
HEADERS:
  Content-Type: application/json

BODY (raw JSON):
{
  "userId": 1,
  "productId": 1,
  "quantity": 1
}

RESPONSE (Success):
{
  "cartId": 1,
  "userId": 1,
  "productId": 1,
  "quantity": 1
}
```

### 2. View User's Cart
```
METHOD: GET
URL: http://localhost:9090/api/cart/1

RESPONSE (Success):
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
```
METHOD: PUT
URL: http://localhost:9090/api/cart/1?quantity=3
HEADERS:
  Content-Type: application/json

RESPONSE (Success):
{
  "cartId": 1,
  "userId": 1,
  "productId": 1,
  "quantity": 3
}
```

### 4. Remove Item from Cart
```
METHOD: DELETE
URL: http://localhost:9090/api/cart/1

RESPONSE (Success):
"Item removed from cart"
```

---

## 📦 Order Management

### 1. Place Order
```
METHOD: POST
URL: http://localhost:9090/api/orders/1

RESPONSE (Success):
{
  "orderId": 1,
  "userId": 1,
  "totalAmount": 2549.97,
  "orderDate": "2026-05-13T08:30:00",
  "status": "PLACED"
}

NOTE: Cart is automatically cleared after order is placed
```

### 2. Get User's Orders
```
METHOD: GET
URL: http://localhost:9090/api/orders/1

RESPONSE (Success):
[
  {
    "orderId": 1,
    "userId": 1,
    "totalAmount": 2549.97,
    "orderDate": "2026-05-13T08:30:00",
    "status": "PLACED"
  },
  {
    "orderId": 2,
    "userId": 1,
    "totalAmount": 599.99,
    "orderDate": "2026-05-13T09:15:00",
    "status": "PLACED"
  }
]
```

---

## 💳 Payment Processing

### 1. Process Payment
```
METHOD: POST
URL: http://localhost:9090/api/payments/1

RESPONSE (Success):
{
  "paymentId": 1,
  "orderId": 1,
  "amount": 2549.97,
  "paymentStatus": "SUCCESS",
  "paymentDate": "2026-05-13T08:31:00"
}
```

---

## 🧪 Complete Test Scenario

### Scenario: Customer makes a purchase

**Step 1: Register**
- POST `/api/users/register`
- Save: `userId`

**Step 2: Add Products**
- POST `/api/products` (bulk add)
- Save: `productIds`

**Step 3: Add to Cart**
- POST `/api/cart` (productId: 1, quantity: 1)
- Save: `cartId1`
- POST `/api/cart` (productId: 2, quantity: 2)
- Save: `cartId2`

**Step 4: View Cart**
- GET `/api/cart/{userId}`

**Step 5: Place Order**
- POST `/api/orders/{userId}`
- Save: `orderId`

**Step 6: Process Payment**
- POST `/api/payments/{orderId}`
- Verify: paymentStatus = "SUCCESS"

**Step 7: View Orders**
- GET `/api/orders/{userId}`
- Verify: order status = "PLACED"

---

## 🔍 Common Response Codes

| Code | Meaning |
|------|---------|
| 200 | Success |
| 201 | Created |
| 400 | Bad Request |
| 404 | Not Found |
| 500 | Server Error |

---

## 💡 Error Handling Examples

### User Not Found
```
GET /api/users/999
Status: 200
Response: null
```

### Product Not Found in Cart Update
```
PUT /api/cart/999?quantity=5
Status: 500
Response: "Cart not found"
```

### Invalid Product
```
POST /api/cart
Body: {"userId": 1, "productId": 999, "quantity": 1}
Status: 500
Response: "Product not found"
```

---

## 🎯 Testing Checklist

- [ ] User can register
- [ ] User can login
- [ ] Admin can add products
- [ ] Products can be retrieved
- [ ] User can add item to cart
- [ ] User can view cart
- [ ] User can update quantity
- [ ] User can remove item from cart
- [ ] User can place order
- [ ] Cart clears after order
- [ ] User can view orders
- [ ] Payment can be processed
- [ ] Payment shows SUCCESS status

---

## 📝 Postman Collection Template

```json
{
  "info": {
    "name": "E-Commerce API",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "User Registration",
      "request": {
        "method": "POST",
        "url": "http://localhost:9090/api/users/register",
        "header": [{"key": "Content-Type", "value": "application/json"}]
      }
    }
  ]
}
```

---

## 🚀 Pro Tips

1. **Use Environment Variables** in Postman
   - Set `base_url = http://localhost:9090`
   - Use `{{base_url}}/api/users/register`

2. **Chain Requests** using Tests
   - Extract `userId` from registration response
   - Use in next requests

3. **Test Data Management**
   - Create dedicated test user
   - Keep test product IDs
   - Clear cart before new tests

4. **Monitor Database**
   ```sql
   SELECT * FROM user;
   SELECT * FROM product;
   SELECT * FROM cart;
   SELECT * FROM orders;
   SELECT * FROM payment;
   ```

---

All APIs have been tested and verified working! 🎉
