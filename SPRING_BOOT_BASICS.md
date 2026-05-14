# Spring Boot Essentials - Complete Basics Guide

## Table of Contents
1. HTTP Status Codes
2. ResponseEntity
3. @Transactional
4. Validation Annotations
5. Exception Handling
6. Logging
7. HTTP Methods
8. Fetch Types (JPA)
9. Bean Scope
10. Content Negotiation

---

## 1. HTTP Status Codes

HTTP status codes indicate the result of an HTTP request. Proper usage is essential for RESTful APIs.

### Common Status Codes

| Code | Name | When to Use |
|------|------|-----------|
| 200 | OK | Successful request |
| 201 | Created | Resource created successfully |
| 204 | No Content | Request succeeded but no content to return |
| 400 | Bad Request | Invalid request parameters |
| 401 | Unauthorized | Authentication required |
| 403 | Forbidden | User not authorized for resource |
| 404 | Not Found | Resource doesn't exist |
| 409 | Conflict | Request conflicts (e.g., duplicate email) |
| 500 | Internal Server Error | Server error occurred |
| 503 | Service Unavailable | Server temporarily unavailable |

### Interview Q&A

**Q: What's the difference between 400 and 404?**
A: 
- 400 Bad Request: Client sent invalid data (e.g., missing fields, wrong format)
- 404 Not Found: Resource doesn't exist (e.g., user ID 999 not in database)

**Q: When should you use 201 vs 200?**
A:
- 201 Created: POST creates new resource (include location header with resource URL)
- 200 OK: General success response, often for GET, PUT, DELETE

**Q: What's 409 Conflict used for?**
A: When request conflicts with server state. Example: trying to register with email that already exists.

---

## 2. ResponseEntity

`ResponseEntity` represents the entire HTTP response including status code, headers, and body.

### Basic Usage

```java
// Simple success response
@GetMapping("/{id}")
public ResponseEntity<Product> getProduct(@PathVariable Long id) {
    Product product = productService.getProduct(id);
    return ResponseEntity.ok(product);  // 200 OK
}

// Created response with 201
@PostMapping
public ResponseEntity<Product> createProduct(@RequestBody Product product) {
    Product saved = productService.save(product);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(saved);  // 201 Created
}

// Not found response
@GetMapping("/{id}")
public ResponseEntity<Product> getProduct(@PathVariable Long id) {
    Optional<Product> product = productRepository.findById(id);
    if (product.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(null);  // 404
    }
    return ResponseEntity.ok(product.get());
}

// No content response
@DeleteMapping("/{id}")
public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
    productService.delete(id);
    return ResponseEntity.noContent().build();  // 204
}

// Custom headers
@GetMapping("/{id}/download")
public ResponseEntity<byte[]> downloadInvoice(@PathVariable Long id) {
    byte[] fileContent = invoiceService.generatePdf(id);
    return ResponseEntity.ok()
        .header("Content-Disposition", "attachment; filename=invoice.pdf")
        .contentType(MediaType.APPLICATION_PDF)
        .body(fileContent);
}
```

### Interview Q&A

**Q: Why use ResponseEntity instead of returning objects directly?**
A: ResponseEntity gives control over:
- HTTP status code (200, 201, 404, etc.)
- Response headers
- Response body
- Makes API predictable and RESTful

**Q: How do you return 201 Created with location header?**
```java
@PostMapping
public ResponseEntity<Product> create(@RequestBody Product p) {
    Product saved = productService.save(p);
    return ResponseEntity.created(
        URI.create("/api/products/" + saved.getId())
    ).body(saved);
}
```

---

## 3. @Transactional

Manages database transactions ensuring data consistency and atomicity.

### Basic Usage

```java
@Service
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    // Without @Transactional - if update fails, cart cleared anyway
    public void placeOrderBad(Long userId) {
        Order order = new Order();
        order.setUserId(userId);
        orderRepository.save(order);
        
        cartRepository.clearByUserId(userId);  // If this fails, order exists but cart doesn't
    }
    
    // With @Transactional - both succeed or both fail
    @Transactional
    public void placeOrder(Long userId) {
        Order order = new Order();
        order.setUserId(userId);
        orderRepository.save(order);
        
        cartRepository.clearByUserId(userId);  // If this fails, entire transaction rolls back
    }
    
    // Rollback on specific exception
    @Transactional(rollbackFor = PaymentException.class)
    public void processPaymentTransaction(Payment payment) throws PaymentException {
        payment.setStatus("PROCESSING");
        paymentRepository.save(payment);
        
        if (!paymentGateway.authorize(payment)) {
            throw new PaymentException("Payment failed");  // Transaction rolls back
        }
        
        payment.setStatus("SUCCESS");
        paymentRepository.save(payment);
    }
    
    // No rollback on specific exception
    @Transactional(noRollbackFor = WarningException.class)
    public void riskOperation() throws WarningException {
        // Some operations
        // If WarningException thrown, transaction commits anyway
    }
    
    // Read-only transaction (slightly more efficient)
    @Transactional(readOnly = true)
    public List<Order> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId);
    }
}
```

### Propagation Levels

```java
// REQUIRED (default) - Use existing transaction or create new
@Transactional(propagation = Propagation.REQUIRED)
public void method1() { }

// REQUIRES_NEW - Always create new transaction, suspend current
@Transactional(propagation = Propagation.REQUIRES_NEW)
public void logAuditTrail(String action) {
    auditRepository.save(new AuditLog(action));
    // Commits independently even if parent transaction fails
}

// SUPPORTS - Use transaction if exists, otherwise none
@Transactional(propagation = Propagation.SUPPORTS)
public void optionalTransaction() { }

// NESTED - Creates savepoint, can rollback partially
@Transactional(propagation = Propagation.NESTED)
public void nestedOperation() { }
```

### Interview Q&A

**Q: Why use @Transactional?**
A: Ensures ACID properties:
- Atomicity: All or nothing
- Consistency: Valid state maintained
- Isolation: Concurrent transactions don't interfere
- Durability: Committed data persists

**Q: What happens without @Transactional?**
A: Each database operation auto-commits. If one fails mid-process, partial data saved (inconsistent state).

**Q: What's the difference between rollbackFor and noRollbackFor?**
A:
- rollbackFor: Specify exceptions that trigger rollback (default only checked exceptions)
- noRollbackFor: Specify exceptions that don't trigger rollback despite error

**Q: When should you use readOnly=true?**
A: For queries only. Slightly optimizes query execution since database knows no updates will occur.

---

## 4. Validation Annotations

Input validation ensures data integrity and prevents invalid data from entering the system.

### Common Validation Annotations

```java
import javax.validation.constraints.*;

@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Name cannot be null")
    @NotBlank(message = "Name cannot be blank")
    @Size(min = 2, max = 50, message = "Name must be 2-50 characters")
    private String name;
    
    @NotNull
    @Email(message = "Email should be valid")
    private String email;
    
    @NotNull
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
    
    @Min(value = 18, message = "Age must be at least 18")
    @Max(value = 100, message = "Age must be less than 100")
    private Integer age;
    
    @Positive(message = "Price must be positive")
    private Double salary;
    
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone must be 10 digits")
    private String phone;
    
    @Future(message = "Date must be in future")
    private LocalDate expiryDate;
    
    @PastOrPresent(message = "Birth date cannot be in future")
    private LocalDate birthDate;
}
```

### Controller with Validation

```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        // @Valid triggers validation
        // If invalid, 400 Bad Request returned automatically
        User saved = userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
    
    // Custom validation response
    @PostMapping
    public ResponseEntity<?> createUserCustom(@Valid @RequestBody User user, 
                                              BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> 
                errors.put(error.getField(), error.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(errors);
        }
        User saved = userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
```

### Custom Validation

```java
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueEmailValidator.class)
public @interface UniqueEmail {
    String message() default "Email already exists";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return !userRepository.existsByEmail(value);
    }
}

// Usage
public class User {
    @UniqueEmail
    private String email;
}
```

### Interview Q&A

**Q: What's the difference between @NotNull and @NotBlank?**
A:
- @NotNull: Value cannot be null (can be empty string)
- @NotBlank: Value cannot be null or empty string

**Q: How do you validate in controller?**
A: Add `@Valid` annotation on `@RequestBody` parameter. Spring auto-validates and returns 400 if invalid.

**Q: What if validation fails?**
A: By default, Spring returns 400 Bad Request with validation error details in response body.

---

## 5. Exception Handling

Global exception handling ensures consistent error responses across the API.

### Global Exception Handler

```java
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    // Handle resource not found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(
            ResourceNotFoundException ex, HttpServletRequest request) {
        
        ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.NOT_FOUND.value(),
            "Resource Not Found",
            ex.getMessage(),
            request.getRequestURI()
        );
        
        logger.warn("Resource not found: {}", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    
    // Handle validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationError(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            fieldErrors.put(error.getField(), error.getDefaultMessage())
        );
        
        ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            "Validation Failed",
            fieldErrors.toString(),
            request.getRequestURI()
        );
        
        logger.warn("Validation error: {}", fieldErrors);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    
    // Handle custom exceptions
    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<ErrorResponse> handlePaymentError(
            PaymentException ex, HttpServletRequest request) {
        
        ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.PAYMENT_REQUIRED.value(),
            "Payment Processing Error",
            ex.getMessage(),
            request.getRequestURI()
        );
        
        logger.error("Payment error: {}", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.PAYMENT_REQUIRED);
    }
    
    // Handle general exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex, HttpServletRequest request) {
        
        ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Internal Server Error",
            "An unexpected error occurred",
            request.getRequestURI()
        );
        
        logger.error("Unexpected error", ex);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

// Error Response DTO
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    
    // Constructor, getters, setters
}

// Custom Exception
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

public class PaymentException extends RuntimeException {
    public PaymentException(String message) {
        super(message);
    }
}
```

### Interview Q&A

**Q: What is @ControllerAdvice?**
A: Handles exceptions globally across all controllers, ensuring consistent error responses.

**Q: Why use global exception handling?**
A: Provides:
- Consistent error response format
- Centralized error handling logic
- Better error logging and monitoring
- Clean controller code

---

## 6. Logging

Logging tracks application behavior and helps debug issues.

### SLF4J with Logback

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    @Autowired
    private UserRepository userRepository;
    
    public User registerUser(User user) {
        logger.info("Attempting to register user with email: {}", user.getEmail());
        
        if (userRepository.existsByEmail(user.getEmail())) {
            logger.warn("Registration failed - email already exists: {}", user.getEmail());
            throw new ResourceConflictException("Email already registered");
        }
        
        try {
            User saved = userRepository.save(user);
            logger.info("User registered successfully with ID: {}", saved.getId());
            return saved;
        } catch (Exception ex) {
            logger.error("Error registering user: {}", user.getEmail(), ex);
            throw ex;
        }
    }
    
    public User getUserById(Long id) {
        logger.debug("Fetching user with ID: {}", id);
        
        User user = userRepository.findById(id)
            .orElseThrow(() -> {
                logger.error("User not found with ID: {}", id);
                return new ResourceNotFoundException("User not found");
            });
        
        logger.debug("User fetched successfully: {}", user.getEmail());
        return user;
    }
}
```

### Log Levels

| Level | Use Case | Example |
|-------|----------|---------|
| TRACE | Detailed diagnostic | Method entry/exit points |
| DEBUG | Development debugging | Variable values, flow |
| INFO | Important events | User registration, payment |
| WARN | Potential issues | Deprecated API usage |
| ERROR | Error conditions | Database connection failed |
| FATAL | Critical errors | System shutdown |

### Interview Q&A

**Q: Which log level should you use?**
A:
- DEBUG: Local development only
- INFO: Important business events (login, payment, order)
- WARN: Something unexpected but recoverable
- ERROR: Something went wrong, needs attention

**Q: Should you log sensitive data?**
A: No. Never log passwords, tokens, or payment card details. Use masks if needed:
```java
logger.info("User login attempt: {}", maskEmail(email));

private String maskEmail(String email) {
    String[] parts = email.split("@");
    return parts[0].charAt(0) + "***@" + parts[1];
}
```

---

## 7. HTTP Methods

Proper HTTP method usage ensures RESTful API design.

### HTTP Methods Reference

```java
@RestController
@RequestMapping("/api/products")
public class ProductController {
    
    // GET - Retrieve resource (safe, idempotent)
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getById(id));
    }
    
    // POST - Create new resource (not idempotent)
    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) {
        Product saved = productService.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
    
    // PUT - Replace entire resource (idempotent)
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id, 
            @Valid @RequestBody Product product) {
        Product updated = productService.update(id, product);
        return ResponseEntity.ok(updated);
    }
    
    // PATCH - Partial update (not idempotent)
    @PatchMapping("/{id}")
    public ResponseEntity<Product> patchProduct(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {
        Product patched = productService.patch(id, updates);
        return ResponseEntity.ok(patched);
    }
    
    // DELETE - Remove resource (idempotent)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
```

### Interview Q&A

**Q: What's the difference between PUT and PATCH?**
A:
- PUT: Replace entire resource (must provide all fields)
- PATCH: Partial update (only changed fields)

**Q: What does idempotent mean?**
A: Multiple identical requests produce same result as single request.
- GET, PUT, DELETE are idempotent
- POST, PATCH are not (multiple POSTs create multiple resources)

---

## 8. Fetch Types (JPA)

Controls when related entities are loaded from database.

```java
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String email;
    
    // LAZY - Load only when accessed (default for OneToMany)
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Order> orders;
    
    // EAGER - Load immediately (default for ManyToOne)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;
}

// Problem: N+1 Query (causes performance issues)
@GetMapping("/users-with-orders")
public ResponseEntity<List<User>> getAllUsersWithOrders() {
    List<User> users = userRepository.findAll();  // 1 query
    for (User user : users) {
        user.getOrders().size();  // N queries (one per user)
    }
    return ResponseEntity.ok(users);
}

// Solution 1: Use JOIN FETCH
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.orders")
    List<User> findAllWithOrders();
}

// Solution 2: Use Projection
public interface UserDTO {
    Long getId();
    String getEmail();
    List<Order> getOrders();
}

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public List<UserDTO> findAllDTO();
}

// Solution 3: Eager loading
@ManyToOne(fetch = FetchType.EAGER)
private Role role;
```

### Interview Q&A

**Q: What's the N+1 problem?**
A: 1 query to fetch N records + N additional queries to load related data = poor performance.

**Q: Should you use EAGER or LAZY by default?**
A: LAZY by default. Only load related data when needed. Use JOIN FETCH or projections to load specific data.

---

## 9. Bean Scope

Controls lifecycle and instance sharing of Spring beans.

```java
// SINGLETON (default) - One instance for entire application
@Component
@Scope("singleton")
public class SingletonService {
    // Single instance shared by all components
}

// PROTOTYPE - New instance for each request
@Component
@Scope("prototype")
public class PrototypeService {
    // New instance created each time injected
}

// REQUEST - New instance per HTTP request
@Component
@Scope("request")
public class RequestService {
    // New instance for each HTTP request
}

// SESSION - One instance per user session
@Component
@Scope("session")
public class SessionService {
    // Shared within same user session
}

// Example: Use case for prototype
@Service
@Scope("prototype")
public class InvoiceGenerator {
    private ByteArrayOutputStream output;
    
    public InvoiceGenerator() {
        output = new ByteArrayOutputStream();
    }
    
    public byte[] generate(Order order) {
        // Each request gets fresh instance, avoiding shared state
        return output.toByteArray();
    }
}

// Inject prototype in singleton
@Service
public class OrderService {
    @Autowired
    private ObjectProvider<InvoiceGenerator> invoiceGeneratorProvider;
    
    public void processOrder(Order order) {
        InvoiceGenerator generator = invoiceGeneratorProvider.getObject();  // New instance
        byte[] pdf = generator.generate(order);
    }
}
```

### Interview Q&A

**Q: What's the default scope?**
A: Singleton - one instance for entire application lifetime.

**Q: When use prototype scope?**
A: When beans hold mutable state that should not be shared (e.g., file streams, report generators).

---

## 10. Content Negotiation

API can return different content types based on client preference.

```java
@RestController
@RequestMapping("/api/products")
public class ProductController {
    
    // Returns JSON by default
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Product> getProductsJson() {
        return productService.getAll();
    }
    
    // Returns XML if client requests
    @GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
    public List<Product> getProductsXml() {
        return productService.getAll();
    }
    
    // Auto-negotiate based on Accept header
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<Product> getProducts() {
        return productService.getAll();
    }
    
    // Consume different formats
    @PostMapping(
        consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Product createProduct(@RequestBody Product product) {
        return productService.save(product);
    }
    
    // Custom content type
    @GetMapping(value = "/{id}/report", produces = "text/plain")
    public String getProductReport(@PathVariable Long id) {
        Product product = productService.getById(id);
        return "Product: " + product.getName() + "\nPrice: " + product.getPrice();
    }
    
    // Download as CSV
    @GetMapping(value = "/export", produces = "text/csv")
    public String exportProductsCsv() {
        List<Product> products = productService.getAll();
        StringBuilder csv = new StringBuilder();
        csv.append("ID,Name,Price\n");
        for (Product p : products) {
            csv.append(p.getId()).append(",").append(p.getName()).append(",").append(p.getPrice()).append("\n");
        }
        return csv.toString();
    }
}

// Configure content negotiation
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer
            .defaultContentType(MediaType.APPLICATION_JSON)
            .mediaType("json", MediaType.APPLICATION_JSON)
            .mediaType("xml", MediaType.APPLICATION_XML)
            .mediaType("csv", MediaType.parseMediaType("text/csv"));
    }
}
```

### Interview Q&A

**Q: What's content negotiation?**
A: Server returns different content types based on client's `Accept` header preference (JSON, XML, CSV, etc.).

**Q: How does it work?**
A: Client sends `Accept: application/xml` header. Server checks `@GetMapping(produces = ...)` and returns XML instead of JSON.

---

## Quick Reference Table

| Topic | Key Concept | Example |
|-------|-------------|---------|
| Status Codes | Use correct codes | 201 for creation, 404 when not found |
| ResponseEntity | Full HTTP control | `ResponseEntity.ok(data)` |
| @Transactional | Atomicity | All changes succeed or all rollback |
| Validation | @Valid on @RequestBody | Automatic 400 response if invalid |
| Exception Handling | @ControllerAdvice | Consistent error responses |
| Logging | SLF4J Logger | Log important events and errors |
| HTTP Methods | RESTful Design | GET, POST, PUT, DELETE for CRUD |
| Fetch Types | Performance | LAZY default, JOIN FETCH for optimization |
| Bean Scope | Lifecycle | Singleton default, Prototype for state |
| Content Negotiation | Flexibility | API supports JSON, XML, CSV, etc. |

---

## Summary

These are the **core Spring Boot basics** that appear in 90%+ of interviews:

1. Always return proper HTTP status codes
2. Use ResponseEntity for control
3. Mark service methods with @Transactional
4. Validate input with @Valid
5. Handle exceptions globally with @ControllerAdvice
6. Log important events (INFO) and errors (ERROR)
7. Use correct HTTP verbs for RESTful design
8. Avoid N+1 queries with careful fetch type usage
9. Be aware of bean scope implications
10. Support content negotiation when appropriate

Master these and you'll ace any Spring Boot interview! 🚀
