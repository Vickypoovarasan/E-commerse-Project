# Model Classes Overview

## Classes Included
- User
- Role
- Product
- Payment
- OrderRequest
- OrderItem
- Order
- CartResponse
- Cart

## Purpose
Model classes represent the application's data structure. They are mapped to database tables (entities) or used as data transfer objects (DTOs).

## Typical Interview Questions & Answers

**Q: What is an entity in Spring Boot?**
A: An entity is a Java class annotated with `@Entity` that maps to a database table.

**Q: What is a DTO?**
A: A Data Transfer Object is a plain Java object used to transfer data between layers, not directly mapped to a database table.

**Q: How do you define a primary key in a model?**
A: With the `@Id` annotation, often combined with `@GeneratedValue` for auto-increment.

## Class Responsibilities
- **User**: Represents a user account, including credentials and roles.
- **Role**: Defines user roles (e.g., ADMIN, CUSTOMER).
- **Product**: Represents a product in the catalog.
- **Payment**: Stores payment transaction details.
- **OrderRequest**: DTO for placing an order.
- **OrderItem**: Represents an item in an order.
- **Order**: Represents a placed order.
- **CartResponse**: DTO for cart details sent to the frontend.
- **Cart**: Represents a user's shopping cart.
