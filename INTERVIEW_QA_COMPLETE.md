# E-Commerce Project - Complete Interview Q&A Guide

## Table of Contents
1. Spring Boot Fundamentals
2. Architecture & Design Patterns
3. REST API Design
4. Database & JPA
5. Spring Security
6. Project-Specific Scenarios
7. Frontend Integration
8. Performance & Best Practices

---

## 1. Spring Boot Fundamentals

### Q: What is Spring Boot and why did you use it for this project?
**A:** Spring Boot is a framework that simplifies Spring application development by providing auto-configuration, embedded servers, and pre-configured dependencies. I used it because:
- Rapid development with minimal boilerplate
- Automatic database schema creation via JPA
- Built-in server (Tomcat) without manual setup
- Convention over configuration approach reduces errors

### Q: Explain @SpringBootApplication annotation.
**A:** `@SpringBootApplication` is a composite annotation that combines:
- `@Configuration` - marks class as a source of bean definitions
- `@EnableAutoConfiguration` - tells Spring to auto-configure components based on classpath
- `@ComponentScan` - scans for @Component, @Service, @Repository classes

It's the entry point for the entire application.

### Q: What is dependency injection and how is it used in your project?
**A:** Dependency injection is a design pattern where objects receive dependencies from external sources rather than creating them. In my project:
- Controllers use `@Autowired` to inject services
- Services use `@Autowired` to inject repositories
- This loose coupling makes code testable and maintainable

Example:
```java
@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
}
```

### Q: What is the difference between @Component, @Service, and @Repository?
**A:**
- `@Component` - generic Spring component
- `@Service` - specialization of @Component for business logic layer
- `@Repository` - specialization of @Component for data access layer with exception translation

They're functionally similar but semantically indicate the layer's purpose.

---

## 2. Architecture & Design Patterns

### Q: Explain the three-layer architecture used in your project.
**A:** The project uses Controller-Service-Repository (three-tier) architecture:

1. **Controller Layer** - Receives HTTP requests, validates input, calls services
2. **Service Layer** - Contains business logic, calculations, coordinations
3. **Repository Layer** - Handles database access and queries

Benefits:
- Separation of concerns
- Easy to test each layer independently
- Changes in one layer don't affect others

### Q: Why is the service layer important?
**A:** The service layer:
- Encapsulates business rules in one place
- Makes code reusable across controllers
- Simplifies testing by isolating business logic
- Allows controllers to remain thin and HTTP-focused

For example, OrderService handles order calculation logic that might be called from multiple endpoints.

### Q: How does the Repository pattern work in your project?
**A:** The Repository pattern abstracts data access logic:
- Repository interfaces extend `JpaRepository`
- Spring Data auto-generates implementations
- Controllers never directly access the database
- Queries can be changed without affecting the rest of the code

Example: `UserRepository` provides methods like `save()`, `findById()`, `delete()` without writing SQL.

### Q: What is the difference between DTO (Data Transfer Object) and Entity?
**A:**
- **Entity** - Mapped to database table, contains JPA annotations
- **DTO** - Plain Java object for transferring data between layers

In my project:
- `OrderRequest` is a DTO for order placement
- `Order` is an entity mapped to the orders table

DTOs prevent exposing internal structure and allow flexible API contracts.

---

## 3. REST API Design

### Q: How did you design the REST endpoints in your project?
**A:** I followed REST principles:
- Resource-based URLs: `/api/users`, `/api/products`, `/api/orders`
- Appropriate HTTP methods:
  - `GET /api/products` - retrieve all
  - `POST /api/products` - create new
  - `PUT /api/products/{id}` - update
  - `DELETE /api/products/{id}` - delete
- Request/response bodies in JSON
- Standard HTTP status codes

### Q: What is @RequestMapping and how did you use it?
**A:** `@RequestMapping` maps HTTP requests to controller methods. In my project:
```java
@RestController
@RequestMapping("/api/products")
public class ProductController {
    @GetMapping
    public List<Product> getAllProducts() { }
}
```

This maps `GET /api/products` to `getAllProducts()`.

### Q: What is @CrossOrigin and why did you use it?
**A:** `@CrossOrigin` enables cross-origin requests, allowing the frontend (served separately) to access the backend API. Without it, browsers block requests from different origins due to CORS restrictions.

### Q: What is the difference between @RequestBody and @PathVariable?
**A:**
- `@RequestBody` - Reads entire HTTP request body and converts to Java object
  ```java
  @PostMapping
  public void createProduct(@RequestBody Product product)
  ```
- `@PathVariable` - Extracts value from URL path
  ```java
  @GetMapping("/{id}")
  public Product getProduct(@PathVariable Long id)
  ```

### Q: How did you handle file downloads (PDF/TXT invoices)?
**A:** The `InvoiceController` returns different content types:
```java
@GetMapping("/{orderId}")
public ResponseEntity<byte[]> getPdfInvoice(@PathVariable Long orderId) {
    byte[] pdfBytes = generatePdf(orderId);
    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_PDF)
        .body(pdfBytes);
}
```

The browser receives the file with correct MIME type and triggers download.

---

## 4. Database & JPA

### Q: How did you configure database connection?
**A:** Through `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_db
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update
```

This connects to MySQL and auto-creates tables on startup.

### Q: What is `spring.jpa.hibernate.ddl-auto=update`?
**A:** It tells Hibernate to automatically manage database schema:
- `create` - drop and recreate tables on startup
- `update` - modify schema if needed (doesn't drop data)
- `validate` - only validate schema matches entities
- `none` - do nothing

I used `update` for development convenience.

### Q: How do you define relationships between entities?
**A:** Using JPA annotations:
- `@OneToMany` - One user has many orders
- `@ManyToOne` - Orders belong to one user
- `@OneToOne` - Orders have one payment
- `@ManyToMany` - Products belong to many categories

Example:
```java
@Entity
public class User {
    @OneToMany(mappedBy = "user")
    private List<Order> orders;
}
```

### Q: How do you write custom queries in JPA?
**A:** Using `@Query` annotation:
```java
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.user.id = ?1")
    List<Order> findByUserId(Long userId);
}
```

Alternatively, Spring Data auto-generates queries from method names:
```java
List<Order> findByUserId(Long userId);
```

### Q: What is the N+1 query problem and how did you avoid it?
**A:** N+1 occurs when fetching a parent and then issuing N queries for children. For example, getting all users and then querying orders for each user = 1 + N queries.

Solution: Use `@Query` with `JOIN FETCH` or lazy loading optimization.

---

## 5. Spring Security

### Q: How did you configure Spring Security?
**A:** In `SecurityConfig.java`:
```java
@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }
}
```

I disabled CSRF since the frontend is trusted and permits all requests for development.

### Q: Why did you disable CSRF?
**A:** CSRF (Cross-Site Request Forgery) protection is important in production. For this development project:
- Frontend and backend communicate directly
- No multi-domain concern
- Simplified API testing
- Token exchange adds complexity

In production, I would enable CSRF and implement token-based security.

### Q: What is CSRF and how does it work?
**A:** CSRF is an attack where a malicious site tricks users into making unwanted requests to another site. Spring Security prevents it using CSRF tokens:
- Server generates a unique token per session
- Token must be included in state-changing requests (POST, PUT, DELETE)
- Server validates token matches session

### Q: How would you implement user authentication?
**A:** I could add:
1. Password encoder: `BCryptPasswordEncoder` to hash passwords
2. JWT tokens: Generate tokens on login, validate on requests
3. Session management: Use `@SessionScope` to manage user sessions

Example:
```java
@PostMapping("/login")
public String login(@RequestBody LoginRequest req) {
    User user = userRepository.findByEmail(req.getEmail());
    if (passwordEncoder.matches(req.getPassword(), user.getPassword())) {
        return jwtProvider.generateToken(user);
    }
}
```

---

## 6. Project-Specific Scenarios

### Q: Walk me through the user registration flow.
**A:**
1. User fills registration form on frontend
2. Frontend sends POST request to `/api/users/register` with email, password, name
3. `UserController.register()` receives request
4. `UserService.registerUser()` validates email not already taken
5. Password is hashed using BCrypt
6. New User entity is created and saved via `UserRepository`
7. Database confirms save, success response sent to frontend
8. User is redirected to login page

### Q: How does the shopping cart work?
**A:**
1. User adds product to cart via frontend
2. `CartController.addToCart()` receives product ID and user ID
3. `CartService.addItem()` checks if product already in cart:
   - If yes: increment quantity
   - If no: create new cart item
4. Cart is updated in database via `CartRepository`
5. Frontend displays updated cart totals

### Q: How does the order placement work?
**A:**
1. User clicks "Place Order" after checkout
2. Frontend sends POST to `/api/orders/{userId}`
3. `OrderController.placeOrder()` receives request
4. `OrderService.placeOrder()`:
   - Validates cart is not empty
   - Calculates order total from cart items
   - Creates Order entity with all items
   - Clears user's cart
   - Saves order to database
5. Order ID returned to frontend
6. User redirected to payment page

### Q: How does invoice generation work?
**A:**
1. User downloads invoice after payment success
2. Frontend calls `/api/invoices/{orderId}` or `/api/invoices/{orderId}/txt`
3. `InvoiceController.generatePdfInvoice()` or `.generateTxtInvoice()`:
   - Fetches order from database
   - Retrieves order items and user details
   - For PDF: Uses PDFBox to create formatted document
   - For TXT: Builds plain text format
   - Returns file as response with correct MIME type
4. Browser triggers download

### Q: How would you handle payment processing?
**A:**
1. User enters payment details on payment page
2. Frontend calls `/api/payments/{orderId}`
3. `PaymentController.processPayment()` receives payment info
4. `PaymentService.processPayment()`:
   - Validates payment amount matches order total
   - Calls external payment gateway (Stripe, etc.)
   - Creates Payment entity with transaction details
   - Updates Payment status to SUCCESS/FAILED
   - If successful: updates Order status to COMPLETED

---

## 7. Frontend Integration

### Q: How is the frontend served?
**A:** Static files are in `src/main/resources/static`. Spring Boot automatically serves them:
- `/` serves `index.html`
- Other files served as-is
- No need for separate frontend server

### Q: How does the frontend interact with the backend?
**A:** Using REST API calls via JavaScript fetch:
```javascript
fetch('/api/products')
    .then(res => res.json())
    .then(data => displayProducts(data));
```

All communication is JSON-based over HTTP.

### Q: How did you style the frontend?
**A:** I created `theme.css` with:
- CSS variables for consistent colors
- Reusable component classes (.btn-custom, .card, etc.)
- Bootstrap 5 for responsive layout
- Flaticon CDN for icons
- Gradient effects and shadows for modern look

### Q: Why use a shared theme.css?
**A:**
- Consistency across all pages
- Easy to update colors or styles globally
- Reduces code duplication
- Easier maintenance

---

## 8. Performance & Best Practices

### Q: What performance optimizations did you implement?
**A:**
1. **Database indexing** - Primary keys auto-indexed, foreign keys indexed
2. **Lazy loading** - JPA loads related entities only when needed
3. **Pagination** - Could implement for large lists to reduce data transfer
4. **Caching** - Spring Cache can cache frequently accessed data
5. **Connection pooling** - Hibernate connection pool reuses connections

### Q: How would you handle large datasets?
**A:**
- Implement pagination: `@RequestParam(defaultValue = "0") int page`
- Use Spring Data's `Pageable`: `Page<Product> findAll(Pageable pageable)`
- Return limited records per page instead of all

Example:
```java
@GetMapping
public Page<Product> getProducts(Pageable pageable) {
    return productRepository.findAll(pageable);
}
```

### Q: How would you add exception handling?
**A:** Using `@ControllerAdvice` and `@ExceptionHandler`:
```java
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException e) {
        return ResponseEntity.status(404).body(new ErrorResponse(e.getMessage()));
    }
}
```

This catches exceptions globally and returns consistent error responses.

### Q: How would you log important events?
**A:** Using SLF4J:
```java
private static final Logger logger = LoggerFactory.getLogger(UserService.class);

public void registerUser(User user) {
    logger.info("Attempting to register user: {}", user.getEmail());
    userRepository.save(user);
    logger.info("User registered successfully: {}", user.getEmail());
}
```

This helps debug issues and track application behavior.

### Q: How would you test your code?
**A:** Using JUnit and Mockito:
```java
@Test
public void testGetAllProducts() {
    when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));
    List<Product> result = productService.getAllProducts();
    assertEquals(2, result.size());
}
```

Test each layer independently to ensure code quality.

### Q: What is a good REST API design principle?
**A:**
- **Stateless** - each request contains all needed info
- **Cacheable** - responses should indicate if cacheable
- **Client-Server** - independent evolution of client and server
- **Consistent** - consistent naming and structure
- **Versioning** - support multiple API versions if needed

### Q: How would you secure the API in production?
**A:**
1. **HTTPS only** - Encrypt all traffic
2. **JWT tokens** - Validate tokens on protected endpoints
3. **CORS restricted** - Only allow trusted origins
4. **Rate limiting** - Prevent abuse with request throttling
5. **Input validation** - Validate all user inputs
6. **SQL injection prevention** - Use parameterized queries (JPA does this)
7. **Authentication** - Require login for sensitive endpoints
8. **Authorization** - Check user permissions before operations

---

## 9. Database Design

### Q: How did you design the database schema?
**A:** Using JPA annotations to define relationships:
- **Users table** - Stores user credentials and info
- **Products table** - Stores product details
- **Cart table** - Stores user's shopping cart items
- **Orders table** - Stores placed orders
- **OrderItems table** - Stores items in each order (one-to-many with Orders)
- **Payments table** - Stores payment transactions

Relationships:
- User → Orders (one-to-many)
- User → Cart (one-to-one)
- Order → OrderItems (one-to-many)
- Order → Payment (one-to-one)

### Q: Where would you add indexes for better performance?
**A:**
- **Users.email** - Frequent login queries
- **Orders.userId** - Fetch user's orders
- **Cart.userId** - Fetch user's cart
- **Products.category** - Filter by category
- **OrderItems.orderId** - Fetch order items

---

## 10. Real-World Scenarios

### Q: A user reports they can't place an order. How would you debug?
**A:**
1. Check application logs for errors
2. Verify cart items exist for the user
3. Test `/api/orders/{userId}` endpoint directly
4. Check database - ensure user, cart, products exist
5. Verify inventory (if tracking stock)
6. Check payment status
7. Review OrderService.placeOrder() logic
8. Test with sample data to isolate issue

### Q: How would you add a new feature like product reviews?
**A:**
1. Create `Review` entity with user, product, rating, comment
2. Create `ReviewRepository` extending JpaRepository
3. Create `ReviewService` with business logic
4. Create `ReviewController` with endpoints
5. Update `Product` entity with `@OneToMany` relationship
6. Add frontend form for review submission
7. Update product detail page to display reviews

### Q: How would you handle stock inventory?
**A:**
1. Add `stockQuantity` field to Product entity
2. In `CartService.addItem()`, check `stockQuantity >= requestedQuantity`
3. In `OrderService.placeOrder()`, decrement stock after order succeeds
4. Alert admin when stock < threshold
5. Implement reorder logic

### Q: How would you prevent duplicate orders?
**A:**
1. Add unique constraint on transaction ID for payments
2. Check if order already exists for same user + same items + same time
3. Use database transactions to ensure atomicity
4. Implement idempotency key on frontend

### Q: How would you scale this application?
**A:**
1. **Database** - Use read replicas, sharding for large datasets
2. **Caching** - Add Redis for session storage and data caching
3. **API Gateway** - Load balance requests across multiple server instances
4. **Microservices** - Split into separate services (users, products, orders, payments)
5. **CDN** - Serve static files from CDN for faster delivery
6. **Async processing** - Use message queues for order processing

---

## Summary of Key Concepts

| Concept | Purpose | Example |
|---------|---------|---------|
| @RestController | Creates REST endpoints | ProductController |
| @RequestMapping | Maps URL to controller | /api/products |
| @Autowired | Inject dependencies | @Autowired ProductService |
| @Entity | Define database entity | Product class |
| @Repository | Data access interface | ProductRepository |
| @Service | Business logic layer | ProductService |
| @RequestBody | Convert JSON to object | POST request body |
| @PathVariable | Extract URL parameter | /products/{id} |
| JpaRepository | Base interface for DB access | extends JpaRepository<E, ID> |
| ResponseEntity | Control HTTP response | ResponseEntity<T> |
