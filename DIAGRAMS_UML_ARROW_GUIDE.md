# UML Diagram Arrow Guide for Draw.io

This document gives exact relationship arrows, cardinality labels, and draw.io drawing notes for the `uml-class-diagram.mmd` and the available diagram PNG files.

## Exact PNG images available
Use the following files as the exact visual reference for manual drawing in draw.io:

- `diagrams/uml-class-diagram.png`
- `diagrams/er-diagram.png`
- `diagrams/data-flow-diagram.png`
- `diagrams/db-connectivity-diagram.png`

## UML class diagram exact relationship connectors
The UML class diagram uses six associations. Draw each as a simple association line in draw.io (no arrowheads), with cardinality labels at the ends and a relationship name on the line.

### 1) User -- Cart
- Mermaid definition: `User "1" -- "0..*" Cart : owns`
- draw.io connector:
  - Line from `User` to `Cart`
  - `User` endpoint label: `1`
  - `Cart` endpoint label: `0..*`
  - Relationship label: `owns`
- Meaning: One user can own zero or many cart items.

### 2) User -- Order
- Mermaid definition: `User "1" -- "0..*" Order : places`
- draw.io connector:
  - Line from `User` to `Order`
  - `User` endpoint label: `1`
  - `Order` endpoint label: `0..*`
  - Relationship label: `places`
- Meaning: One user can place zero or many orders.

### 3) Product -- Cart
- Mermaid definition: `Product "1" -- "0..*" Cart : referenced by`
- draw.io connector:
  - Line from `Product` to `Cart`
  - `Product` endpoint label: `1`
  - `Cart` endpoint label: `0..*`
  - Relationship label: `referenced by`
- Meaning: One product can be referenced in zero or many cart items.

### 4) Order -- OrderItem
- Mermaid definition: `Order "1" -- "0..*" OrderItem : contains`
- draw.io connector:
  - Line from `Order` to `OrderItem`
  - `Order` endpoint label: `1`
  - `OrderItem` endpoint label: `0..*`
  - Relationship label: `contains`
- Meaning: One order can contain zero or many order items.

### 5) Product -- OrderItem
- Mermaid definition: `Product "1" -- "0..*" OrderItem : referenced by`
- draw.io connector:
  - Line from `Product` to `OrderItem`
  - `Product` endpoint label: `1`
  - `OrderItem` endpoint label: `0..*`
  - Relationship label: `referenced by`
- Meaning: One product can be referenced in zero or many order items.

### 6) Order -- Payment
- Mermaid definition: `Order "1" -- "0..1" Payment : paid by`
- draw.io connector:
  - Line from `Order` to `Payment`
  - `Order` endpoint label: `1`
  - `Payment` endpoint label: `0..1`
  - Relationship label: `paid by`
- Meaning: One order may be paid by zero or one payment.

## Draw.io exact shape and text setup
1. Open draw.io.
2. Enable the `UML` shape library.
3. Add class boxes for these entities:
   - `User`
   - `Product`
   - `Cart`
   - `Order`
   - `OrderItem`
   - `Payment`
4. Add attributes exactly as shown in `uml-class-diagram.mmd`:
   - `User`: `userId`, `username`, `password`, `role`, `email`
   - `Product`: `productId`, `name`, `description`, `price`, `stockQuantity`, `category`
   - `Cart`: `cartId`, `userId`, `productId`, `quantity`
   - `Order`: `orderId`, `userId`, `totalAmount`, `orderDate`, `status`, `shippingAddress`, `deliveryNote`
   - `OrderItem`: `orderItemId`, `orderId`, `productId`, `productName`, `unitPrice`, `quantity`, `totalPrice`
   - `Payment`: `paymentId`, `orderId`, `amount`, `paymentStatus`, `paymentDate`
5. Connect the classes with plain association lines.
6. Set endpoint labels exactly as shown in the relationship definitions above.
7. Add the relationship names as text on the lines.

## Exact arrow mark reference for draw.io
- `User` -> `Cart`: label `owns`, cardinality `1` at `User`, `0..*` at `Cart`
- `User` -> `Order`: label `places`, cardinality `1` at `User`, `0..*` at `Order`
- `Product` -> `Cart`: label `referenced by`, cardinality `1` at `Product`, `0..*` at `Cart`
- `Order` -> `OrderItem`: label `contains`, cardinality `1` at `Order`, `0..*` at `OrderItem`
- `Product` -> `OrderItem`: label `referenced by`, cardinality `1` at `Product`, `0..*` at `OrderItem`
- `Order` -> `Payment`: label `paid by`, cardinality `1` at `Order`, `0..1` at `Payment`

## Notes
- The Mermaid syntax does not use arrowheads for these relationships. Use simple UML association lines in draw.io.
- The PNG files in `diagrams/` are the exact generated visuals; use them to match spacing and layout.
- For best accuracy, copy the labels and multiplicities exactly as shown.
