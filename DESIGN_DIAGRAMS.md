# E-Commerce Application Design Diagrams

This document provides the key architectural diagrams for the e-commerce application:
- UML Class Diagram
- ER Diagram
- Data Flow Diagram
- Database Connectivity Diagram

Each diagram includes relationship cardinalities and clear connection semantics.

---

## 1. UML Class Diagram

### Description
The UML class diagram shows the main entity classes in the application and their cardinality relationships.

### Classes and Relationships
- `User` 1 -> 0..* `Cart`
- `User` 1 -> 0..* `Order`
- `Product` 1 -> 0..* `Cart`
- `Order` 1 -> 0..* `OrderItem`
- `Product` 1 -> 0..* `OrderItem`
- `Order` 1 -> 0..1 `Payment`

```mermaid
classDiagram
    class User {
        +Long userId
        +String username
        +String password
        +Role role
        +String email
    }
    class Product {
        +Long productId
        +String name
        +String description
        +double price
        +int stockQuantity
        +String category
    }
    class Cart {
        +Long cartId
        +Long userId
        +Long productId
        +int quantity
    }
    class Order {
        +Long orderId
        +Long userId
        +double totalAmount
        +LocalDateTime orderDate
        +String status
        +String shippingAddress
        +String deliveryNote
    }
    class OrderItem {
        +Long orderItemId
        +Long orderId
        +Long productId
        +String productName
        +double unitPrice
        +int quantity
        +double totalPrice
    }
    class Payment {
        +Long paymentId
        +Long orderId
        +double amount
        +String paymentStatus
        +LocalDateTime paymentDate
    }

    User "1" -- "0..*" Cart : owns
    User "1" -- "0..*" Order : places
    Product "1" -- "0..*" Cart : referenced by
    Order "1" -- "0..*" OrderItem : contains
    Product "1" -- "0..*" OrderItem : referenced by
    Order "1" -- "0..1" Payment : paid by
```

---

## 2. ER Diagram

### Description
The ER diagram displays the database tables and the foreign key relationships between them.

### Tables and Cardinalities
- `USER` 1 -> 0..* `CART`
- `USER` 1 -> 0..* `ORDERS`
- `PRODUCT` 1 -> 0..* `CART`
- `ORDERS` 1 -> 0..* `ORDER_ITEM`
- `PRODUCT` 1 -> 0..* `ORDER_ITEM`
- `ORDERS` 1 -> 0..1 `PAYMENT`

```mermaid
erDiagram
    USER {
        long userId PK
        string username
        string password
        string role
        string email
    }
    PRODUCT {
        long productId PK
        string name
        string description
        double price
        int stockQuantity
        string category
    }
    CART {
        long cartId PK
        long userId FK
        long productId FK
        int quantity
    }
    ORDERS {
        long orderId PK
        long userId FK
        double totalAmount
        datetime orderDate
        string status
        string shippingAddress
        string deliveryNote
    }
    ORDER_ITEM {
        long orderItemId PK
        long orderId FK
        long productId FK
        string productName
        double unitPrice
        int quantity
        double totalPrice
    }
    PAYMENT {
        long paymentId PK
        long orderId FK
        double amount
        string paymentStatus
        datetime paymentDate
    }

    USER ||--o{ CART : "has"
    USER ||--o{ ORDERS : "places"
    PRODUCT ||--o{ CART : "referenced by"
    ORDERS ||--o{ ORDER_ITEM : "contains"
    PRODUCT ||--o{ ORDER_ITEM : "included in"
    ORDERS ||--|| PAYMENT : "paid by"
```

---

## 3. Data Flow Diagram

### Description
This DFD shows how the user interacts with frontend pages and how requests flow through controllers, services, repositories, and the database.

### Flow Summary
- User interacts with frontend pages.
- Frontend calls API controllers.
- Controllers call business services.
- Services delegate persistence to repositories.
- Repositories read/write the database.

```mermaid
flowchart LR
    subgraph User/Browser
      U[User / Admin]
    end
    subgraph Frontend
      UI[Static HTML Pages]
    end
    subgraph Backend
      PC[ProductController]
      CC[CartController]
      OC[OrderController]
      PAYC[PaymentController]
      INV[InvoiceController]
      PS[ProductService]
      CS[CartService]
      OS[OrderService]
      PAYS[PaymentService]
      PR[ProductRepository]
      CR[CartRepository]
      OR[OrderRepository]
      PYR[PaymentRepository]
      OIR[OrderItemRepository]
    end
    subgraph Database
      DB[(MySQL Database)]
    end

    U -->|browse products| UI
    UI -->|GET /api/products| PC
    PC --> PS
    PS --> PR
    UI -->|add to cart| CC
    CC --> CS
    CS --> CR
    UI -->|checkout| OC
    OC --> OS
    OS --> OR
    OC --> OIR
    UI -->|pay order| PAYC
    PAYC --> PAYS
    PAYS --> PYR
    UI -->|download invoice| INV
    INV --> OR
    INV --> OIR
    PR --> DB
    CR --> DB
    OR --> DB
    PYR --> DB
    OIR --> DB
```

---

## 4. Database Connectivity Diagram

### Description
This diagram shows how application layers connect to the database and where the data is stored.

### Connectivity Summary
- Controllers connect to Services.
- Services connect to Repositories.
- Repositories connect to the MySQL database.
- Static pages communicate with controllers via REST.

```mermaid
flowchart TB
    UI[Frontend Pages]
    PC[ProductController]
    CC[CartController]
    OC[OrderController]
    PAYC[PaymentController]
    INV[InvoiceController]
    PS[ProductService]
    CS[CartService]
    OS[OrderService]
    PAYS[PaymentService]
    PR[ProductRepository]
    CR[CartRepository]
    OR[OrderRepository]
    PYR[PaymentRepository]
    OIR[OrderItemRepository]
    DB[(MySQL Database)]

    UI --> PC
    UI --> CC
    UI --> OC
    UI --> PAYC
    UI --> INV
    PC --> PS
    CC --> CS
    OC --> OS
    PAYC --> PAYS
    INV --> OS
    PS --> PR
    CS --> CR
    OS --> OR
    OS --> OIR
    PAYS --> PYR
    PR --> DB
    CR --> DB
    OR --> DB
    PYR --> DB
    OIR --> DB
```

---

## 5. Relationship Summary

### Entity Cardinalities
- `User` 1 — 0..* `Cart`
- `User` 1 — 0..* `Order`
- `Product` 1 — 0..* `Cart`
- `Order` 1 — 0..* `OrderItem`
- `Product` 1 — 0..* `OrderItem`
- `Order` 1 — 0..1 `Payment`

### Database Keys
- `USER.userId` is PK
- `PRODUCT.productId` is PK
- `CART.cartId` is PK, `userId` and `productId` are FKs
- `ORDERS.orderId` is PK, `userId` is FK
- `ORDER_ITEM.orderItemId` is PK, `orderId` and `productId` are FKs
- `PAYMENT.paymentId` is PK, `orderId` is FK

---

## 6. Notes
- Because `Order` uses a separate `OrderItem` table, the order-to-items relationship is one-to-many.
- The shopping cart table stores item quantity with a many-to-one reference to product and user.
- Invoice generation is based on `Order` and `OrderItem` data.
- The database connectivity diagram shows the core path from frontend to database.
