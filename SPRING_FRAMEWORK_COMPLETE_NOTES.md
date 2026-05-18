# Complete Spring Framework Notes

## Table of Contents
1. [Introduction to Spring](#introduction-to-spring)
2. [What is Spring Framework](#what-is-spring-framework)
3. [Core Concepts](#core-concepts)
4. [How Spring Works Internally](#how-spring-works-internally)
5. [Beans and Bean Files](#beans-and-bean-files)
6. [Spring IoC Container](#spring-ioc-container)
7. [Dependency Injection](#dependency-injection)
8. [Spring MVC](#spring-mvc)
9. [Spring JDBC](#spring-jdbc)
10. [Spring ORM](#spring-orm)
11. [Spring Hibernate Integration](#spring-hibernate-integration)
12. [AOP (Aspect-Oriented Programming)](#aop-aspect-oriented-programming)
13. [Spring Boot](#spring-boot)

---

## Introduction to Spring

Spring Framework is one of the most popular and powerful frameworks in the Java ecosystem. It provides comprehensive infrastructure support for developing Java applications. Spring handles the infrastructure so that you can focus on your application logic.

### Key Features:
- **Lightweight and Non-Intrusive**: Uses Plain Old Java Objects (POJOs)
- **Flexible**: Can be used for web applications, RESTful services, batch processing, etc.
- **Modular**: Can use individual modules without using the entire framework
- **Testable**: Built with testing as a core principle

---

## What is Spring Framework

Spring Framework is a comprehensive framework for building enterprise-grade applications in Java. It simplifies the development of web applications and services by providing:

### Main Benefits:
1. **Inversion of Control (IoC)** - Manages object lifecycle
2. **Dependency Injection (DI)** - Loose coupling between components
3. **Aspect-Oriented Programming (AOP)** - Cross-cutting concerns
4. **Transaction Management** - Declarative and programmatic transaction support
5. **Data Access** - Integration with databases
6. **Web MVC Framework** - For building web applications
7. **Security** - Comprehensive security features

### Spring Modules:

```
Spring Framework Modules:
├── Core Container
│   ├── spring-core
│   ├── spring-beans
│   ├── spring-context
│   └── spring-expression
├── Data Access/Integration
│   ├── spring-jdbc
│   ├── spring-orm
│   ├── spring-oxm
│   └── spring-jms
├── Web
│   ├── spring-web
│   ├── spring-webmvc
│   ├── spring-webflux
│   └── spring-websocket
├── AOP and Instrumentation
│   ├── spring-aop
│   └── spring-aspects
└── Testing
    └── spring-test
```

---

## Core Concepts

### 1. **Inversion of Control (IoC)**

IoC is a design principle where the control of object creation and lifecycle is transferred from your code to the framework.

**Traditional Approach (Tight Coupling):**
```java
public class UserService {
    private UserRepository repository = new UserRepository(); // Hard-coded dependency
    
    public User getUser(int id) {
        return repository.findById(id);
    }
}
```

**Spring IoC Approach (Loose Coupling):**
```java
public class UserService {
    private UserRepository repository; // Injected by Spring
    
    public UserService(UserRepository repository) {
        this.repository = repository;
    }
    
    public User getUser(int id) {
        return repository.findById(id);
    }
}
```

### 2. **Dependency Injection (DI)**

DI is a technique to achieve IoC. Instead of creating dependencies inside a class, Spring injects them.

**Types of Dependency Injection:**

#### a) Constructor Injection
```java
@Component
public class UserService {
    private final UserRepository repository;
    
    // Constructor injection
    public UserService(UserRepository repository) {
        this.repository = repository;
    }
}
```

#### b) Setter Injection
```java
@Component
public class UserService {
    private UserRepository repository;
    
    // Setter injection
    @Autowired
    public void setRepository(UserRepository repository) {
        this.repository = repository;
    }
}
```

#### c) Field Injection
```java
@Component
public class UserService {
    @Autowired
    private UserRepository repository; // Field injection
}
```

**Best Practice:** Constructor injection is preferred as it makes dependencies explicit and enables immutability.

### 3. **Beans**

A bean is a Java object that is instantiated, assembled, and managed by the Spring IoC container.

**Characteristics:**
- Managed by Spring Container
- Has lifecycle callbacks (initialization, destruction)
- Can be configured via XML, Java config, or annotations
- Scope can be defined (singleton, prototype, session, request, etc.)

### 4. **Spring Container (IoC Container)**

The Spring container is responsible for:
- Creating bean instances
- Wiring beans using DI
- Managing the lifecycle of beans
- Handling bean destruction

**Types of Containers:**
- `BeanFactory` - Basic container
- `ApplicationContext` - Advanced container (preferred)

---

## How Spring Works Internally

### Spring Application Lifecycle:

```
┌─────────────────────────────────────────────────────────┐
│ 1. Spring Application Starts                            │
└──────────────────┬──────────────────────────────────────┘
                   ↓
┌─────────────────────────────────────────────────────────┐
│ 2. ApplicationContext is Created                        │
│    - Reads configuration files (XML/Java Config)       │
│    - Scans for @Component, @Service, @Repository      │
└──────────────────┬──────────────────────────────────────┘
                   ↓
┌─────────────────────────────────────────────────────────┐
│ 3. Bean Definition Processing                          │
│    - Collects all bean definitions                     │
│    - Registers them in registry                        │
└──────────────────┬──────────────────────────────────────┘
                   ↓
┌─────────────────────────────────────────────────────────┐
│ 4. Bean Instantiation                                  │
│    - Creates instances of beans                        │
│    - Based on their scope (singleton, prototype, etc.) │
└──────────────────┬──────────────────────────────────────┘
                   ↓
┌─────────────────────────────────────────────────────────┐
│ 5. Dependency Injection                                │
│    - Injects dependencies into beans                   │
│    - Constructor, Setter, Field injection              │
└──────────────────┬──────────────────────────────────────┘
                   ↓
┌─────────────────────────────────────────────────────────┐
│ 6. Bean Post Processing                                │
│    - Calls lifecycle callbacks                         │
│    - @PostConstruct, InitializingBean, init-method    │
│    - Proxies are created for AOP                       │
└──────────────────┬──────────────────────────────────────┘
                   ↓
┌─────────────────────────────────────────────────────────┐
│ 7. Application Ready                                   │
│    - Beans are ready to use                            │
└──────────────────┬──────────────────────────────────────┘
                   ↓
         [Application Running]
                   ↓
┌─────────────────────────────────────────────────────────┐
│ 8. Bean Destruction (On Shutdown)                      │
│    - Calls @PreDestroy, DisposableBean, destroy-method│
└─────────────────────────────────────────────────────────┘
```

### Detailed Explanation:

#### Step 1-2: Container Creation
Spring creates `ApplicationContext` which reads configuration:
- XML files (`applicationContext.xml`)
- Java configuration classes (`@Configuration`)
- Annotations (`@Component`, `@Service`, etc.)

#### Step 3: Bean Definition
- Collects metadata about beans
- Stores in `BeanDefinitionRegistry`
- Each bean has metadata: class name, scope, dependencies, etc.

#### Step 4: Bean Instantiation
```java
// If scope = singleton (default)
Bean instance = Class.forName(beanClassName).newInstance();

// If scope = prototype
// New instance created each time bean is requested
```

#### Step 5: Dependency Injection
```java
// Spring analyzes dependencies
// Injects via constructor, setter, or field
// Resolves by type, name, or qualifier
```

#### Step 6: Post-Processing
```java
@Component
public class MyBean {
    @PostConstruct
    public void init() {
        // Called after bean is created and dependencies injected
    }
    
    @PreDestroy
    public void cleanup() {
        // Called when application shuts down
    }
}
```

---

## Beans and Bean Files

### What is a Bean?

A bean is an object managed by Spring container. It's the fundamental unit in a Spring application.

**Example:**
```java
// Without Spring (Manual Management)
UserRepository repo = new UserRepository();
UserService service = new UserService(repo);
// You're responsible for managing lifecycle

// With Spring
@Repository
public class UserRepository {
    // Spring manages this bean
}

@Service
public class UserService {
    @Autowired
    private UserRepository repository;
    // Spring injects the repository
}
```

### Bean Configuration Methods:

#### 1. XML Configuration (Legacy)

**applicationContext.xml:**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd">

    <!-- Simple Bean Definition -->
    <bean id="userRepository" class="com.example.repository.UserRepository"/>
    
    <!-- Bean with Constructor Injection -->
    <bean id="userService" class="com.example.service.UserService">
        <constructor-arg ref="userRepository"/>
    </bean>
    
    <!-- Bean with Setter Injection -->
    <bean id="emailService" class="com.example.service.EmailService">
        <property name="smtpHost" value="smtp.gmail.com"/>
        <property name="port" value="587"/>
    </bean>
    
</beans>
```

#### 2. Java Configuration (Recommended)

**AppConfig.java:**
```java
@Configuration
public class AppConfig {
    
    @Bean
    public UserRepository userRepository() {
        return new UserRepository();
    }
    
    @Bean
    public UserService userService(UserRepository userRepository) {
        return new UserService(userRepository);
    }
    
    @Bean
    public EmailService emailService() {
        EmailService service = new EmailService();
        service.setSmtpHost("smtp.gmail.com");
        service.setPort(587);
        return service;
    }
}
```

#### 3. Annotation-Based Configuration (Modern)

**UserRepository.java:**
```java
@Repository
public class UserRepository {
    // Automatically registered as a bean
}

@Service
public class UserService {
    @Autowired
    private UserRepository repository;
    
    public User getUser(int id) {
        return repository.findById(id);
    }
}
```

### Bean Lifecycle:

```
Bean Lifecycle Phases:
1. Instantiation: Spring creates bean instance
2. Population: Spring injects dependencies
3. Initialization: Custom init logic (@PostConstruct)
4. Usage: Bean is available for use
5. Destruction: Custom cleanup logic (@PreDestroy)
```

**Example:**
```java
@Component
public class MyBean implements InitializingBean, DisposableBean {
    
    @PostConstruct
    public void initMethod() {
        System.out.println("Bean initialized using @PostConstruct");
    }
    
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("Bean initialized using InitializingBean");
    }
    
    @PreDestroy
    public void destroyMethod() {
        System.out.println("Bean destroyed using @PreDestroy");
    }
    
    @Override
    public void destroy() throws Exception {
        System.out.println("Bean destroyed using DisposableBean");
    }
}
```

### Bean Scopes:

```java
1. Singleton (Default)
@Scope("singleton")
@Component
public class SingletonBean {
    // Single instance for entire application
}

2. Prototype
@Scope("prototype")
@Component
public class PrototypeBean {
    // New instance each time requested
}

3. Request
@Scope("request")
@Component
public class RequestBean {
    // New instance for each HTTP request
}

4. Session
@Scope("session")
@Component
public class SessionBean {
    // New instance for each HTTP session
}

5. Application (ServletContext)
@Scope("application")
@Component
public class ApplicationBean {
    // Shared across all sessions
}
```

---

## Spring IoC Container

### ApplicationContext Architecture:

```
       ApplicationContext
              │
       ┌──────┴──────┐
       ↓             ↓
  ClassPathXmlApplicationContext
  AnnotationConfigApplicationContext
```

### Creating Application Context:

```java
// XML-based
ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

// Java config-based
ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

// Spring Boot (Automatic)
// Automatically creates ApplicationContext when app starts
```

### Retrieving Beans:

```java
ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

// By bean name
UserService service1 = context.getBean("userService", UserService.class);

// By class type
UserService service2 = context.getBean(UserService.class);

// Get all beans of a type
Map<String, UserService> services = context.getBeansOfType(UserService.class);
```

### Container Responsibilities:

1. **Bean Creation**: Instantiates beans
2. **Dependency Resolution**: Identifies and injects dependencies
3. **Lifecycle Management**: Calls init and destroy methods
4. **Configuration Metadata**: Reads configuration
5. **Event Publishing**: Publishes application events
6. **Message Resolution**: Handles i18n

---

## Dependency Injection

### DI Principles:

1. **Separation of Concerns**: Components don't create their dependencies
2. **Loose Coupling**: Changes to dependencies don't affect components
3. **Testability**: Easy to provide mock dependencies
4. **Reusability**: Components can be reused with different implementations

### Dependency Resolution:

```java
// Spring uses multiple strategies to resolve dependencies:

// 1. By Type (Primary)
@Service
public class UserService {
    @Autowired
    private UserRepository repository; // Resolved by type
}

// 2. By Name (Secondary)
@Service
public class UserService {
    @Autowired
    @Qualifier("userRepositoryImpl")
    private UserRepository repository; // Resolved by name
}

// 3. Explicit Annotation
@Service
public class UserService {
    @Autowired(required = false)
    private Optional<AuditService> auditService; // Optional dependency
}
```

### Circular Dependency Issue:

```java
// Problem: Circular dependency
@Service
public class ServiceA {
    @Autowired
    private ServiceB serviceB; // ServiceB depends on ServiceA
}

@Service
public class ServiceB {
    @Autowired
    private ServiceA serviceA; // Circular reference
}

// Solution: Use field injection or setter injection (not constructor)
@Service
public class ServiceA {
    @Autowired
    private ServiceB serviceB; // Injected after construction
}

// OR use ObjectProvider
@Service
public class ServiceA {
    private final ObjectProvider<ServiceB> serviceB;
    
    public ServiceA(ObjectProvider<ServiceB> serviceB) {
        this.serviceB = serviceB;
    }
}
```

---

## Spring MVC

### What is MVC?

MVC (Model-View-Controller) is an architectural pattern that separates application into three components:

```
        Request
           ↓
     ┌─────────────┐
     │ Controller  │ ← Handles user input
     └──────┬──────┘
            ↓
     ┌─────────────┐
     │   Model     │ ← Contains business logic & data
     └──────┬──────┘
            ↓
     ┌─────────────┐
     │    View     │ ← Displays data to user
     └──────┬──────┘
            ↓
        Response
```

### Spring MVC Request Flow:

```
1. Client sends HTTP Request
              ↓
2. DispatcherServlet receives request
              ↓
3. HandlerMapping determines controller
              ↓
4. HandlerAdapter calls appropriate handler method
              ↓
5. Controller executes business logic
              ↓
6. Model is populated with data
              ↓
7. ViewResolver determines view to render
              ↓
8. View is rendered with model data
              ↓
9. Response sent back to client
```

### Spring MVC Example:

**Configuration (Spring Boot):**
```java
@SpringBootApplication
public class EcommerceApplication {
    public static void main(String[] args) {
        SpringApplication.run(EcommerceApplication.class, args);
    }
}
```

**Controller:**
```java
@Controller
public class ProductController {
    
    @Autowired
    private ProductService productService;
    
    @GetMapping("/products")
    public String getAllProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "products"; // View name
    }
    
    @GetMapping("/products/{id}")
    public String getProduct(@PathVariable int id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "product-detail";
    }
    
    @PostMapping("/products")
    public String saveProduct(@ModelAttribute Product product) {
        productService.saveProduct(product);
        return "redirect:/products";
    }
    
    @DeleteMapping("/products/{id}")
    public String deleteProduct(@PathVariable int id) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }
}
```

**Model:**
```java
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "price")
    private double price;
    
    @Column(name = "description")
    private String description;
    
    // Getters and Setters
}
```

**View (Thymeleaf Template):**
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Products</title>
</head>
<body>
    <h1>Products</h1>
    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Price</th>
            </tr>
        </thead>
        <tbody>
            <tr th:each="product : ${products}">
                <td th:text="${product.id}">1</td>
                <td th:text="${product.name}">Product Name</td>
                <td th:text="${product.price}">Price</td>
            </tr>
        </tbody>
    </table>
</body>
</html>
```

### REST API with Spring MVC:

```java
@RestController
@RequestMapping("/api/products")
public class ProductRestController {
    
    @Autowired
    private ProductService productService;
    
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable int id) {
        Optional<Product> product = productService.getProductById(id);
        return product.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product savedProduct = productService.saveProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable int id,
            @RequestBody Product productDetails) {
        Product updatedProduct = productService.updateProduct(id, productDetails);
        return ResponseEntity.ok(updatedProduct);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully");
    }
}
```

---

## Spring JDBC

### What is Spring JDBC?

Spring JDBC provides a template-based approach to database operations, reducing boilerplate code compared to traditional JDBC.

### Traditional JDBC vs Spring JDBC:

**Traditional JDBC:**
```java
Connection conn = null;
PreparedStatement stmt = null;
ResultSet rs = null;

try {
    conn = DriverManager.getConnection("jdbc:mysql://...", "user", "pass");
    stmt = conn.prepareStatement("SELECT * FROM users WHERE id = ?");
    stmt.setInt(1, id);
    rs = stmt.executeQuery();
    
    if (rs.next()) {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setName(rs.getString("name"));
        return user;
    }
} catch (SQLException e) {
    e.printStackTrace();
} finally {
    // Close resources
    if (rs != null) rs.close();
    if (stmt != null) stmt.close();
    if (conn != null) conn.close();
}
```

**Spring JDBC:**
```java
@Repository
public class UserRepository {
    
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public User getUserById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new UserRowMapper(), id);
    }
}
```

### Spring JDBC Configuration:

**application.properties:**
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce
spring.datasource.username=root
spring.datasource.password=password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

**Spring Boot Auto-Configuration:**
```java
// JdbcTemplate is automatically configured and injected
@Repository
public class UserRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    // Methods...
}
```

### JdbcTemplate Methods:

```java
@Repository
public class UserRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    // 1. Query for single object
    public User getUserById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new UserRowMapper(), id);
    }
    
    // 2. Query for list
    public List<User> getAllUsers() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, new UserRowMapper());
    }
    
    // 3. Query for single value
    public int getUserCount() {
        String sql = "SELECT COUNT(*) FROM users";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }
    
    // 4. Insert/Update/Delete (UPDATE query)
    public void saveUser(User user) {
        String sql = "INSERT INTO users (name, email) VALUES (?, ?)";
        jdbcTemplate.update(sql, user.getName(), user.getEmail());
    }
    
    // 5. Batch operations
    public void saveUsers(List<User> users) {
        String sql = "INSERT INTO users (name, email) VALUES (?, ?)";
        List<Object[]> batchArgs = new ArrayList<>();
        
        for (User user : users) {
            batchArgs.add(new Object[]{user.getName(), user.getEmail()});
        }
        
        jdbcTemplate.batchUpdate(sql, batchArgs);
    }
    
    // 6. Call stored procedure
    public void callStoredProcedure(int userId) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
            .withProcedureName("get_user_details");
        
        MapSqlParameterSource in = new MapSqlParameterSource()
            .addValue("user_id", userId);
        
        Map<String, Object> out = jdbcCall.executeFunction(Map.class, in);
    }
}
```

### RowMapper:

```java
// Custom RowMapper to map database rows to Java objects
public class UserRowMapper implements RowMapper<User> {
    
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setUserType(rs.getString("user_type"));
        
        return user;
    }
}
```

### NamedParameterJdbcTemplate:

```java
@Repository
public class UserRepository {
    
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    
    public User getUserByNameAndEmail(String name, String email) {
        String sql = "SELECT * FROM users WHERE name = :name AND email = :email";
        
        MapSqlParameterSource params = new MapSqlParameterSource()
            .addValue("name", name)
            .addValue("email", email);
        
        return namedParameterJdbcTemplate.queryForObject(sql, params, new UserRowMapper());
    }
}
```

---

## Spring ORM

### What is Spring ORM?

Spring ORM provides integration with popular Object-Relational Mapping (ORM) frameworks like Hibernate, JPA, and others. It offers transaction management and simplified database operations.

### ORM Frameworks Supported:

1. **Hibernate** - Most popular ORM framework
2. **JPA (Java Persistence API)** - Standard specification
3. **MyBatis** - SQL mapping framework
4. **Apache OpenJPA** - JPA implementation

### Spring ORM Architecture:

```
Application Layer
     ↓
Spring ORM Layer (Transaction Management, Session Management)
     ↓
ORM Framework Layer (Hibernate/JPA)
     ↓
JDBC Layer
     ↓
Database
```

### Advantages Over Spring JDBC:

| Aspect | Spring JDBC | Spring ORM |
|--------|------------|-----------|
| Mapping | Manual | Automatic |
| Relationships | Manual handling | Automatic navigation |
| Queries | SQL writing | HQL/Criteria queries |
| Performance | Good | Optimized with caching |
| Complexity | Simple | More features |

---

## Spring Hibernate Integration

### What is Hibernate?

Hibernate is a powerful ORM framework that maps Java objects to database tables, providing:
- Automatic table mapping
- Relationship handling (One-to-One, One-to-Many, Many-to-Many)
- Query optimization
- Caching mechanism
- Transaction management

### Hibernate Configuration:

**application.properties:**
```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce
spring.datasource.username=root
spring.datasource.password=password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Hibernate Configuration
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Optional: Lazy loading
spring.jpa.properties.hibernate.enable_lazy_load_no_trans=true
```

### Entity Mapping:

**Basic Entity:**
```java
@Entity
@Table(name = "products")
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    @Column(name = "price", nullable = false)
    private double price;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructors, Getters, Setters
}
```

### Entity Relationships:

#### One-to-Many Relationship:

```java
// Parent Entity
@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "name")
    private String name;
    
    // One user has many orders
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();
    
    // Getters and Setters
}

// Child Entity
@Entity
@Table(name = "orders")
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "order_date")
    private LocalDateTime orderDate;
    
    // Many orders belong to one user
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    // Getters and Setters
}
```

#### One-to-One Relationship:

```java
@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "name")
    private String name;
    
    // One user has one wallet
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Wallet wallet;
}

@Entity
@Table(name = "wallets")
public class Wallet {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "balance")
    private double balance;
    
    // One wallet belongs to one user
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
```

#### Many-to-Many Relationship:

```java
@Entity
@Table(name = "products")
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "name")
    private String name;
    
    // Many products in many carts
    @ManyToMany(mappedBy = "products", fetch = FetchType.LAZY)
    private List<Cart> carts = new ArrayList<>();
}

@Entity
@Table(name = "carts")
public class Cart {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "user_id")
    private int userId;
    
    // Many carts have many products
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "cart_products",
        joinColumns = @JoinColumn(name = "cart_id"),
        inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products = new ArrayList<>();
}
```

### JPA Repository:

```java
// Interface extending JpaRepository
// Spring automatically provides CRUD operations
@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    
    // Custom query methods
    List<Product> findByName(String name);
    
    Product findByIdAndName(int id, String name);
    
    List<Product> findByPriceGreaterThan(double price);
    
    List<Product> findByNameContainingIgnoreCase(String keyword);
    
    // Custom JPQL query
    @Query("SELECT p FROM Product p WHERE p.price BETWEEN ?1 AND ?2")
    List<Product> findProductsByPrice(double minPrice, double maxPrice);
    
    // Custom native SQL query
    @Query(value = "SELECT * FROM products WHERE price > :price", nativeQuery = true)
    List<Product> findExpensiveProducts(@Param("price") double price);
    
    // Delete operation
    @Modifying
    @Query("DELETE FROM Product p WHERE p.id = ?1")
    void deleteProductById(int id);
}
```

### Service Layer with Hibernate:

```java
@Service
public class ProductService {
    
    @Autowired
    private ProductRepository repository;
    
    @Autowired
    private UserRepository userRepository;
    
    // Create
    public Product createProduct(Product product) {
        return repository.save(product);
    }
    
    // Read
    public List<Product> getAllProducts() {
        return repository.findAll();
    }
    
    public Product getProductById(int id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }
    
    // Update
    @Transactional
    public Product updateProduct(int id, Product productDetails) {
        Product product = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        
        product.setName(productDetails.getName());
        product.setPrice(productDetails.getPrice());
        product.setDescription(productDetails.getDescription());
        
        return repository.save(product);
    }
    
    // Delete
    public void deleteProduct(int id) {
        repository.deleteById(id);
    }
    
    // Search
    public List<Product> searchProducts(String keyword) {
        return repository.findByNameContainingIgnoreCase(keyword);
    }
    
    // Price filter
    public List<Product> getProductsByPriceRange(double minPrice, double maxPrice) {
        return repository.findProductsByPrice(minPrice, maxPrice);
    }
}
```

### Cascade Types:

```java
// DETACH - Don't cascade detach
// MERGE - Cascade merge (update operations)
// PERSIST - Cascade persist (save operations)
// REFRESH - Cascade refresh
// REMOVE - Cascade delete
// ALL - All of the above

@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
private List<Order> orders;

// OR

@OneToMany(cascade = CascadeType.ALL)
private List<Order> orders;
```

### Fetch Types:

```java
// LAZY - Data loaded when accessed (Default for collections)
@OneToMany(fetch = FetchType.LAZY)
private List<Order> orders; // Loaded only when orders.get() is called

// EAGER - Data loaded immediately (Default for associations)
@ManyToOne(fetch = FetchType.EAGER)
private User user; // Loaded immediately when entity is fetched
```

### Transaction Management:

```java
@Service
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private PaymentRepository paymentRepository;
    
    // Method runs in a transaction
    @Transactional
    public Order createOrder(Order order) {
        // All operations here are in one transaction
        Order savedOrder = orderRepository.save(order);
        
        Payment payment = new Payment();
        payment.setOrder(savedOrder);
        paymentRepository.save(payment);
        
        // If exception occurs, entire transaction rolls back
        return savedOrder;
    }
    
    // Read-only transaction (optimized)
    @Transactional(readOnly = true)
    public Order getOrder(int id) {
        return orderRepository.findById(id).orElse(null);
    }
    
    // Propagation control
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logAudit(String message) {
        // Runs in new transaction, separate from caller's transaction
    }
}
```

---

## AOP (Aspect-Oriented Programming)

### What is AOP?

AOP is a programming paradigm that helps handle cross-cutting concerns like logging, security, transactions, etc., separately from business logic.

### Cross-Cutting Concerns:

- Logging
- Security
- Transaction management
- Caching
- Exception handling
- Performance monitoring

### AOP Concepts:

```
1. Aspect - Reusable module of cross-cutting concern
2. Join Point - Point in program execution (method call, field access)
3. Pointcut - Expression matching join points
4. Advice - Code executed at join points
5. Weaving - Process of applying aspects to target objects
```

### Advice Types:

```java
@Aspect
@Component
public class LoggingAspect {
    
    // 1. Before Advice - Executed before method execution
    @Before("execution(* com.example.service.*.*(..))")
    public void beforeMethod(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        System.out.println("Before executing: " + methodName);
    }
    
    // 2. After Returning Advice - Executed after successful execution
    @AfterReturning(pointcut = "execution(* com.example.service.*.*(..))", 
                    returning = "result")
    public void afterReturning(JoinPoint joinPoint, Object result) {
        System.out.println("Method returned: " + result);
    }
    
    // 3. After Throwing Advice - Executed when exception is thrown
    @AfterThrowing(pointcut = "execution(* com.example.service.*.*(..))", 
                   throwing = "exception")
    public void afterThrowing(JoinPoint joinPoint, Exception exception) {
        System.out.println("Exception: " + exception.getMessage());
    }
    
    // 4. After (Finally) Advice - Executed after method execution (success/exception)
    @After("execution(* com.example.service.*.*(..))")
    public void afterMethod(JoinPoint joinPoint) {
        System.out.println("After method execution");
    }
    
    // 5. Around Advice - Executed around method execution
    @Around("execution(* com.example.service.*.*(..))")
    public Object aroundMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("Before method");
        long startTime = System.currentTimeMillis();
        
        try {
            Object result = joinPoint.proceed(); // Execute actual method
            return result;
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            System.out.println("Method took: " + duration + "ms");
        }
    }
}
```

### Pointcut Expressions:

```java
// 1. Execution pointcut
execution(public * *(..))                  // All public methods
execution(* get*(..))                      // All methods starting with 'get'
execution(public * com.example.service.*.*(..))  // All public methods in service package

// 2. Within pointcut
within(com.example.service.*)              // All methods in service package
within(com.example.service.UserService)    // All methods in UserService class

// 3. Args pointcut
args(String, int)                          // Methods with String and int parameters

// 4. Annotation pointcut
@annotation(com.example.annotation.Loggable)  // Methods with @Loggable annotation

// 5. Combined pointcuts
execution(* com.example.service.*.*(..)) && args(String)
execution(public * *(..)) && !within(com.example.config.*)
```

### Custom Annotation for AOP:

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Loggable {
    String value() default "Logging";
}

@Aspect
@Component
public class LoggingAspect {
    
    @Before("@annotation(loggable)")
    public void logMethod(JoinPoint joinPoint, Loggable loggable) {
        System.out.println(loggable.value() + " - " + joinPoint.getSignature());
    }
}

// Usage
@Service
public class UserService {
    
    @Loggable("Getting user details")
    public User getUser(int id) {
        // Method implementation
        return user;
    }
}
```

---

## Spring Boot

### What is Spring Boot?

Spring Boot is a framework that simplifies Spring application development by providing:
- Auto-configuration
- Embedded servers (Tomcat, Jetty)
- Starter dependencies
- Opinionated defaults
- Production-ready features

### Spring Boot Advantages:

```
1. Rapid Application Development - Get started quickly
2. Auto-Configuration - Automatic configuration based on classpath
3. Embedded Server - No need for external application server
4. Starter Dependencies - Simplified dependency management
5. Actuator - Built-in monitoring and management endpoints
6. Production Ready - Logging, metrics, health checks
```

### Spring Boot Project Structure:

```
my-app/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/myapp/
│   │   │       ├── MyApplication.java
│   │   │       ├── controller/
│   │   │       ├── service/
│   │   │       ├── repository/
│   │   │       ├── model/
│   │   │       ├── config/
│   │   │       ├── exception/
│   │   │       └── util/
│   │   ├── resources/
│   │   │   ├── application.properties
│   │   │   ├── application-dev.properties
│   │   │   ├── application-prod.properties
│   │   │   ├── static/
│   │   │   └── templates/
│   └── test/
│       └── java/
└── pom.xml
```

### Basic Spring Boot Application:

```java
@SpringBootApplication
public class EcommerceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(EcommerceApplication.class, args);
    }
}
```

### application.properties:

```properties
# Server Configuration
server.port=8080
server.servlet.context-path=/api

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce
spring.datasource.username=root
spring.datasource.password=password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate Configuration
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Logging
logging.level.root=INFO
logging.level.com.example.ecommerce=DEBUG

# Jackson JSON
spring.jackson.serialization.indent-output=true

# Actuator
management.endpoints.web.exposure.include=health,metrics,env
```

### Spring Boot Starter Dependencies:

```xml
<!-- Web Development -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- Data JPA -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- MySQL Driver -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.33</version>
</dependency>

<!-- Security -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<!-- Testing -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

### Spring Boot Actuator:

```properties
# Enable actuator endpoints
management.endpoints.web.exposure.include=*
management.endpoint.health.show-details=always

# Custom endpoints
management.endpoints.web.base-path=/actuator
```

**Available Endpoints:**
- `/actuator/health` - Application health
- `/actuator/metrics` - Application metrics
- `/actuator/env` - Environment properties
- `/actuator/beans` - Bean registry
- `/actuator/mappings` - Request mappings
- `/actuator/loggers` - Logger configuration

### Spring Boot Testing:

```java
@SpringBootTest
public class UserServiceTests {
    
    @Autowired
    private UserService userService;
    
    @MockBean
    private UserRepository userRepository;
    
    @Test
    public void testGetUser() {
        // Arrange
        User user = new User(1, "John", "john@example.com");
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        
        // Act
        User result = userService.getUser(1);
        
        // Assert
        assertEquals("John", result.getName());
        verify(userRepository, times(1)).findById(1);
    }
}
```

---

## Summary

### Spring Framework provides:

1. **IoC Container** - Manages object lifecycle and dependencies
2. **Dependency Injection** - Loose coupling between components
3. **MVC Framework** - Web application development
4. **JDBC Support** - Simplified database operations
5. **ORM Integration** - Hibernate and JPA integration
6. **AOP** - Cross-cutting concerns
7. **Transaction Management** - Declarative and programmatic
8. **Security** - Authentication and authorization
9. **Testing Support** - Unit and integration testing
10. **Spring Boot** - Rapid application development

### Best Practices:

1. Use constructor injection for required dependencies
2. Use @Qualifier for resolving ambiguous beans
3. Make use of Spring profiles for environment-specific configuration
4. Use @Transactional for database operations
5. Implement proper exception handling
6. Use Spring's AOP for cross-cutting concerns
7. Keep your service layer logic separate from controllers
8. Use logging frameworks (SLF4J)
9. Write unit tests for business logic
10. Use Spring Boot for new projects

---

## Glossary

| Term | Definition |
|------|-----------|
| Bean | Object managed by Spring container |
| IoC | Transfer of control to framework |
| DI | Injection of dependencies into objects |
| Pointcut | Expression matching join points in AOP |
| Aspect | Module of cross-cutting concern |
| Entity | JPA mapped Java class representing database table |
| Repository | Data access abstraction |
| Service | Business logic layer |
| Controller | Request handling layer |
| Weaving | Process of applying AOP aspects |

---

**Created**: May 16, 2026
**Framework**: Spring Framework (5.x/6.x)
**Java Version**: Java 8+
