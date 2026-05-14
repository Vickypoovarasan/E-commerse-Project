# Service Classes Overview

## Classes Included
- PaymentService
- ProductService
- CartService
- UserService
- OrderService

## Purpose
Service classes contain business logic. They process data, apply rules, and coordinate between controllers and repositories.

## Typical Interview Questions & Answers

**Q: What is the role of a service class?**
A: Service classes encapsulate business logic and are called by controllers to perform operations.

**Q: How do you inject a repository into a service?**
A: Using `@Autowired` on a repository field or constructor.

**Q: Why separate service and controller logic?**
A: To keep controllers thin and focused on HTTP, while services handle business rules and data processing.

## Class Responsibilities
- **PaymentService**: Handles payment processing and validation.
- **ProductService**: Manages product-related business logic.
- **CartService**: Handles cart operations and calculations.
- **UserService**: Manages user registration, authentication, and profile logic.
- **OrderService**: Handles order placement, status, and history.
