# Repository Classes Overview

## Classes Included
- PaymentRepository
- UserRepository
- ProductRepository
- OrderRepository
- OrderItemRepository
- CartRepository

## Purpose
Repositories are interfaces that handle data access. They extend Spring Data JPA interfaces like `JpaRepository` or `CrudRepository` to provide CRUD operations without boilerplate code.

## Typical Interview Questions & Answers

**Q: What is a repository in Spring Boot?**
A: A repository is an interface that abstracts data access logic, usually by extending `JpaRepository` or `CrudRepository`.

**Q: How does Spring Data JPA implement repository methods?**
A: It generates implementations at runtime based on method names and annotations.

**Q: How do you define a custom query?**
A: With the `@Query` annotation or by following Spring Data naming conventions.

## Class Responsibilities
- **PaymentRepository**: Manages payment data persistence.
- **UserRepository**: Handles user data access.
- **ProductRepository**: Manages product data.
- **OrderRepository**: Handles order data.
- **OrderItemRepository**: Manages order item records.
- **CartRepository**: Handles cart data persistence.
