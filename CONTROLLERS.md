# Controller Classes Overview

## Classes Included
- UserController
- ProductController
- PaymentController
- OrderController
- InvoiceController
- CartController

## Purpose
Controllers handle HTTP requests, map them to service methods, and return responses (usually JSON). They define REST API endpoints for the frontend or external clients.

## Typical Interview Questions & Answers

**Q: What is the role of a controller in Spring Boot?**
A: Controllers receive HTTP requests, process input, call service methods, and return responses. They are annotated with `@RestController` or `@Controller`.

**Q: How do you map a URL to a controller method?**
A: Using annotations like `@RequestMapping`, `@GetMapping`, `@PostMapping`, etc.

**Q: How do you handle cross-origin requests?**
A: With `@CrossOrigin` annotation on the controller or method.

**Q: How do you inject a service into a controller?**
A: Using `@Autowired` on a service field or constructor.

## Class Responsibilities
- **UserController**: Handles user registration, login, and profile endpoints.
- **ProductController**: Manages product CRUD operations and product listing.
- **PaymentController**: Handles payment processing and payment status.
- **OrderController**: Manages order placement and order history.
- **InvoiceController**: Generates and serves invoice files (PDF, TXT).
- **CartController**: Handles cart add, update, remove, and view operations.
