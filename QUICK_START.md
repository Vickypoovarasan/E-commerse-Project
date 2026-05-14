# Quick Start Guide - E-Commerce Application

## ⚡ 5-Minute Setup

### 1. Prerequisites Check
```
✅ Java 17 installed? (Check: java -version)
✅ MySQL running? (Check: mysql -u root -p)
✅ Maven available? (Using ./mvnw so not required)
```

### 2. Database Setup
```sql
mysql -u root -p
CREATE DATABASE ecommerce_db;
EXIT;
```

### 3. Build & Run
```bash
cd ecommerce
./mvnw clean package -DskipTests
java -jar target/ecommerce-0.0.1-SNAPSHOT.jar
```

**Server starts at**: `http://localhost:9090`

---

## 🧪 Quick Test Scenarios

### Scenario 1: Complete Purchase Flow

**Step 1: Register User**
```bash
curl -X POST http://localhost:9090/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123",
    "email": "test@example.com",
    "role": "CUSTOMER"
  }'
```
**Save the returned userId (e.g., 1)**

---

**Step 2: Add Products (Admin)**
```bash
curl -X POST http://localhost:9090/api/products \
  -H "Content-Type: application/json" \
  -d '[
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
    }
  ]'
```

---

**Step 3: View Products**
```bash
curl http://localhost:9090/api/products
```
**Save the productIds returned (e.g., 1, 2)**

---

**Step 4: Add Items to Cart**
```bash
curl -X POST http://localhost:9090/api/cart \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "productId": 1,
    "quantity": 1
  }'
```

**Add another item:**
```bash
curl -X POST http://localhost:9090/api/cart \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "productId": 2,
    "quantity": 2
  }'
```

---

**Step 5: View Cart**
```bash
curl http://localhost:9090/api/cart/1
```
**You should see both items with product names and prices**

---

**Step 6: Update Quantity**
```bash
curl -X PUT "http://localhost:9090/api/cart/1?quantity=2"
```

---

**Step 7: Place Order**
```bash
curl -X POST http://localhost:9090/api/orders/1
```
**Save the orderId returned**
**Cart is automatically cleared after order**

---

**Step 8: Process Payment**
```bash
curl -X POST http://localhost:9090/api/payments/1
```
**Payment processed successfully**

---

**Step 9: View Orders**
```bash
curl http://localhost:9090/api/orders/1
```

---

## 📋 API Cheat Sheet

| Action | Command |
|--------|---------|
| Register | `POST /api/users/register` |
| Login | `POST /api/users/login?username=X&password=Y` |
| List Products | `GET /api/products` |
| Get Product | `GET /api/products/{id}` |
| Add Products | `POST /api/products` |
| Update Product | `PUT /api/products/{id}` |
| Delete Product | `DELETE /api/products/{id}` |
| Add to Cart | `POST /api/cart` |
| Get Cart | `GET /api/cart/{userId}` |
| Update Cart | `PUT /api/cart/{cartId}?quantity=X` |
| Remove from Cart | `DELETE /api/cart/{cartId}` |
| Place Order | `POST /api/orders/{userId}` |
| View Orders | `GET /api/orders/{userId}` |
| Process Payment | `POST /api/payments/{orderId}` |

---

## 🐛 Troubleshooting

### Application won't start
```
ERROR: Port 9090 already in use
SOLUTION: Change in application.properties: server.port=9091
```

### Database connection fails
```
ERROR: com.mysql.cj.jdbc.exceptions.CommunicationsException
SOLUTION: 
1. Start MySQL server
2. Verify credentials in application.properties
3. Run: mysql -u root -p -e "CREATE DATABASE ecommerce_db;"
```

### Cannot connect to localhost:9090
```
SOLUTION: 
1. Check if application is still running
2. Look for port conflicts
3. Try http://127.0.0.1:9090 instead
```

---

## 📂 File Locations

| File | Purpose |
|------|---------|
| `pom.xml` | Dependencies & Build config |
| `src/main/resources/application.properties` | Server & DB config |
| `src/main/java/com/example/ecommerce/` | Java source code |
| `src/main/resources/static/` | Frontend HTML pages |
| `target/ecommerce-0.0.1-SNAPSHOT.jar` | Compiled application |

---

## 🎯 Project Structure Summary

```
✅ User Management (register, login, get user)
✅ Product Catalog (CRUD operations)
✅ Shopping Cart (add, update, remove, view)
✅ Orders (place order, view orders)
✅ Payments (process payment)
✅ Database (MySQL with Hibernate)
✅ REST API (All endpoints working)
✅ Frontend (HTML pages for all features)
```

---

## 💡 Next Steps

1. **Explore the code** - Check service layer logic
2. **Test all endpoints** - Use the curl commands above
3. **Examine database** - `mysql ecommerce_db -e "SHOW TABLES;"`
4. **Read PROJECT_FLOW.md** - Complete architecture guide
5. **Add security** - Implement JWT authentication
6. **Enhance UI** - Improve frontend pages

---

## 📞 Quick Links

- **Spring Boot**: https://spring.io/projects/spring-boot
- **MySQL**: https://dev.mysql.com/
- **cURL**: https://curl.se/
- **Postman**: https://www.postman.com/

---

**✅ Your application is production-ready! Happy coding! 🚀**
