# Java Notes

This file summarizes core Java concepts, including Object-Oriented Programming (OOP), collections, exception handling, multithreading, and synchronization.

---

## 1. Java Basics

### 1.1 Java Platform
- Java is a platform-independent, object-oriented programming language.
- Java programs compile to bytecode and run on the Java Virtual Machine (JVM).
- Java Editions:
  - Java SE (Standard Edition)
  - Java EE / Jakarta EE (Enterprise Edition)
  - Java ME (Micro Edition)

### 1.2 Java Syntax
- Every Java application starts with a `main` method:
  ```java
public class App {
    public static void main(String[] args) {
        System.out.println("Hello, Java!");
    }
}
```
- Java is strongly typed: each variable has a declared data type.
- Primitive types: `int`, `long`, `double`, `float`, `boolean`, `char`, `byte`, `short`.
- Reference types: classes, interfaces, arrays.

---

## 2. Object-Oriented Programming (OOP)

Java is built around OOP principles. The core concepts are:

### 2.1 Class and Object
- A class is a blueprint for objects.
- An object is an instance of a class.

Example:
```java
public class User {
    private String name;
    private int age;

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```

### 2.2 Encapsulation
- Encapsulation hides internal state and requires access through methods.
- Use `private` fields and `public` getters/setters.

Example:
```java
public class Account {
    private double balance;

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }
}
```

### 2.3 Inheritance
- Inheritance lets one class extend another.
- The subclass inherits fields and methods from the superclass.
- Use `extends` keyword.

Example:
```java
public class Animal {
    public void speak() {
        System.out.println("Animal sound");
    }
}

public class Dog extends Animal {
    @Override
    public void speak() {
        System.out.println("Woof");
    }
}
```

### 2.4 Polymorphism
- Polymorphism allows methods to behave differently based on the object type.
- Method overriding enables runtime polymorphism.
- Method overloading enables compile-time polymorphism.

Example:
```java
public class Shape {
    public void draw() {
        System.out.println("Drawing shape");
    }
}

public class Circle extends Shape {
    @Override
    public void draw() {
        System.out.println("Drawing circle");
    }
}

Shape shape = new Circle();
shape.draw(); // Draws circle
```

### 2.5 Abstraction
- Abstraction hides complex implementation details behind simple APIs.
- Use abstract classes and interfaces.

Example:
```java
public abstract class Vehicle {
    public abstract void start();
}

public class Car extends Vehicle {
    @Override
    public void start() {
        System.out.println("Car started");
    }
}
```

---

## 3. Java Interfaces and Abstract Classes

### 3.1 Interface
- Interfaces declare method signatures and constants.
- A class can implement multiple interfaces.
- Java 8 onwards supports default and static methods in interfaces.

Example:
```java
public interface Drivable {
    void drive();

    default void stop() {
        System.out.println("Stopping vehicle");
    }
}

public class Bike implements Drivable {
    @Override
    public void drive() {
        System.out.println("Bike driving");
    }
}
```

### 3.2 Abstract Class
- Abstract classes can have both abstract and concrete methods.
- Use when subclasses share a common base but need custom behavior.

Example:
```java
public abstract class Employee {
    private String name;

    public Employee(String name) {
        this.name = name;
    }

    public abstract double calculateSalary();

    public String getName() {
        return name;
    }
}
```

### 3.3 Marker Interface
- Marker interfaces do not contain methods.
- They mark a class as supporting a behavior.
- Example: `Serializable`, `Cloneable`.

Example:
```java
public class Product implements Serializable {
    private String name;
}
```

---

## 4. Java Collections Framework

The collections framework provides data structures to store and manage groups of objects.

### 4.1 Core Interfaces
- `Collection` — root interface for data structures that hold elements.
- `List` — ordered, indexed collection. Allows duplicates.
- `Set` — unordered collection with unique elements.
- `Queue` — ordered elements for processing.
- `Map` — key-value pairs.

### 4.2 Common Implementations
- `ArrayList` — dynamic array, fast random access.
- `LinkedList` — double-linked list, efficient insert/remove.
- `HashSet` — hash table, unique elements.
- `TreeSet` — sorted set.
- `HashMap` — hash table for key-value pairs.
- `TreeMap` — sorted map.
- `LinkedHashMap` — insertion-ordered map.

### 4.3 Example Usage
```java
List<String> list = new ArrayList<>();
list.add("apple");
list.add("banana");

Set<String> set = new HashSet<>();
set.add("apple");
set.add("apple"); // duplicate ignored

Map<String, Integer> map = new HashMap<>();
map.put("apple", 1);
map.put("banana", 2);
```

### 4.4 Iteration
- For-each loop:
  ```java
for (String item : list) {
    System.out.println(item);
}
```
- Iterator:
  ```java
Iterator<String> iterator = list.iterator();
while (iterator.hasNext()) {
    String item = iterator.next();
}
```
- Streams (Java 8+):
  ```java
list.stream()
    .filter(s -> s.startsWith("a"))
    .forEach(System.out::println);
```

### 4.5 Synchronization and Thread-Safe Collections
- `Vector`, `Hashtable` are synchronized legacy collections.
- Use `Collections.synchronizedList(...)` for thread-safe wrappers.
- Use concurrent collections for multi-threaded access:
  - `ConcurrentHashMap`
  - `CopyOnWriteArrayList`
  - `BlockingQueue`

---

## 5. Exception Handling

### 5.1 Exception Types
- `Throwable` is the superclass of all errors and exceptions.
- `Exception` is checked and must be handled or declared.
- `RuntimeException` is unchecked and does not require explicit handling.
- `Error` represents serious JVM problems.

### 5.2 Try-Catch-Finally
```java
try {
    int result = 10 / 0;
} catch (ArithmeticException e) {
    System.out.println("Cannot divide by zero");
} finally {
    System.out.println("Always runs");
}
```

### 5.3 Try-With-Resources
- Automatically closes resources that implement `AutoCloseable`.

Example:
```java
try (BufferedReader reader = new BufferedReader(new FileReader("file.txt"))) {
    String line = reader.readLine();
} catch (IOException e) {
    e.printStackTrace();
}
```

### 5.4 Throwing Exceptions
- Use `throw` to raise an exception.
- Use `throws` in method signature to declare checked exceptions.

Example:
```java
public void validate(int age) throws IllegalArgumentException {
    if (age < 0) {
        throw new IllegalArgumentException("Age must be positive");
    }
}
```

### 5.5 Custom Exceptions
- Create custom exception classes by extending `Exception` or `RuntimeException`.

Example:
```java
public class InvalidOrderException extends RuntimeException {
    public InvalidOrderException(String message) {
        super(message);
    }
}
```

---

## 6. Java Multithreading

### 6.1 Thread Basics
- A thread is a lightweight subprocess.
- Java supports multithreading with `Thread` and `Runnable`.

### 6.2 Creating Threads
- Extend `Thread`:
  ```java
public class MyThread extends Thread {
    @Override
    public void run() {
        System.out.println("Thread running");
    }
}

MyThread thread = new MyThread();
thread.start();
```
- Implement `Runnable`:
  ```java
public class MyTask implements Runnable {
    @Override
    public void run() {
        System.out.println("Task running");
    }
}

Thread thread = new Thread(new MyTask());
thread.start();
```

### 6.3 Thread Lifecycle
- New
- Runnable
- Running
- Blocked/Waiting
- Terminated

### 6.4 Thread Methods
- `start()` begins execution.
- `run()` contains thread code.
- `join()` waits for the thread to finish.
- `sleep(milliseconds)` pauses the thread.
- `interrupt()` signals interruption.

### 6.5 Executors and Thread Pools
- Use `ExecutorService` for managing threads.

Example:
```java
ExecutorService executor = Executors.newFixedThreadPool(4);
executor.submit(() -> System.out.println("Task executed"));
executor.shutdown();
```

### 6.6 Callable and Future
- `Callable` returns a result and can throw exceptions.
- `Future` retrieves the result later.

Example:
```java
Callable<Integer> task = () -> 5 + 3;
Future<Integer> future = executor.submit(task);
int result = future.get();
```

---

## 7. Synchronization and Concurrency

### 7.1 The Problem of Race Conditions
- When multiple threads access shared data simultaneously, results may be inconsistent.

Example:
```java
public class Counter {
    private int count;

    public void increment() {
        count++;
    }
}
```

### 7.2 Synchronized Methods and Blocks
- Use `synchronized` to protect shared data.

Example:
```java
public class Counter {
    private int count;

    public synchronized void increment() {
        count++;
    }

    public synchronized int getCount() {
        return count;
    }
}
```

- Use synchronized block for finer control:
  ```java
public void add(int value) {
    synchronized (this) {
        count += value;
    }
}
```

### 7.3 Locks and ReentrantLock
- `ReentrantLock` provides more control than `synchronized`.

Example:
```java
Lock lock = new ReentrantLock();

public void safeIncrement() {
    lock.lock();
    try {
        count++;
    } finally {
        lock.unlock();
    }
}
```

### 7.4 Volatile
- `volatile` ensures visibility of changes across threads.
- Use for simple flags and read/write variables.

Example:
```java
private volatile boolean running = true;
```

### 7.5 Wait and Notify
- Use `wait()`, `notify()`, and `notifyAll()` for thread coordination.

Example:
```java
synchronized (lock) {
    while (!condition) {
        lock.wait();
    }
    // process
}

synchronized (lock) {
    condition = true;
    lock.notify();
}
```

### 7.6 Java Concurrency Utilities
- Use high-level classes from `java.util.concurrent`:
  - `CountDownLatch`
  - `CyclicBarrier`
  - `Semaphore`
  - `ConcurrentHashMap`
  - `BlockingQueue`

---

## 8. Java I/O and Files

### 8.1 Input/Output Streams
- Byte streams: `InputStream`, `OutputStream`.
- Character streams: `Reader`, `Writer`.

### 8.2 File I/O Example
```java
try (BufferedReader reader = new BufferedReader(new FileReader("file.txt"))) {
    String line;
    while ((line = reader.readLine()) != null) {
        System.out.println(line);
    }
} catch (IOException e) {
    e.printStackTrace();
}
```

### 8.3 NIO (New I/O)
- Use `java.nio.file` for modern file operations.

Example:
```java
Path path = Paths.get("file.txt");
List<String> lines = Files.readAllLines(path);
Files.write(path, Arrays.asList("line1", "line2"));
```

---

## 9. Java 8 Features

### 9.1 Lambda Expressions
- Concise syntax for anonymous functions.

Example:
```java
List<String> list = Arrays.asList("a", "b", "c");
list.forEach(s -> System.out.println(s));
```

### 9.2 Streams API
- Process collections in a functional style.

Example:
```java
List<String> result = list.stream()
    .filter(s -> s.startsWith("a"))
    .collect(Collectors.toList());
```

### 9.3 Optional
- Use `Optional` to avoid `NullPointerException`.

Example:
```java
Optional<String> name = Optional.ofNullable(getName());
name.ifPresent(System.out::println);
```

---

## 10. Java Memory Model and Garbage Collection

### 10.1 Heap and Stack
- The stack stores method frames and local variables.
- The heap stores objects and arrays.

### 10.2 Garbage Collection
- JVM automatically reclaims unused objects.
- Common collectors: Serial, Parallel, CMS, G1.
- Use `System.gc()` only as a suggestion, not a command.

---

## 11. Java Best Practices

- Use meaningful names for classes, methods, and variables.
- Keep methods short and focused.
- Favor immutability when possible.
- Use interfaces for abstraction.
- Handle exceptions properly and avoid swallowing them.
- Avoid public fields; use getters/setters.
- Prefer `List` over `ArrayList` in declarations.
- Keep thread safety in mind when sharing state.

---

## 12. Useful Java Keywords

- `this` — refers to current object.
- `super` — refers to parent class.
- `final` — prevents reassignment or inheritance.
- `static` — belongs to class, not instance.
- `transient` — excludes field from serialization.
- `volatile` — ensures visibility across threads.
- `synchronized` — coordinates thread access.
- `instanceof` — checks object type.
- `enum` — defines a fixed set of constants.

---

## 13. Summary

These notes cover Java fundamentals, OOP concepts, collections, exception handling, multithreading, and synchronization. Keep this file as a reference while coding Java applications and learning advanced concepts.
