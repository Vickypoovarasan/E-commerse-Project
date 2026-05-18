# MySQL Complete Notes

## Table of Contents
1. [DDL Commands](#ddl-commands)
2. [DML Commands](#dml-commands)
3. [DCL Commands](#dcl-commands)
4. [DTL/TCL Commands](#dtltcl-commands)
5. [Aggregate Functions](#aggregate-functions)
6. [String Functions & Substring](#string-functions--substring)
7. [Predefined Methods & Built-in Functions](#predefined-methods--built-in-functions)
8. [Indexes](#indexes)
9. [Interview SQL Questions](#interview-sql-questions)

---

## DDL Commands

DDL (Data Definition Language) commands are used to create, modify, and delete database structures.

### 1. CREATE TABLE

**Definition**: Creates a new table in the database with specified columns and data types.

**Syntax**:
```sql
CREATE TABLE table_name (
    column_name1 data_type [constraints],
    column_name2 data_type [constraints],
    ...
);
```

**Example**:
```sql
CREATE TABLE employees (
    emp_id INT PRIMARY KEY AUTO_INCREMENT,
    emp_name VARCHAR(100) NOT NULL,
    department VARCHAR(50),
    salary DECIMAL(10, 2),
    hire_date DATE,
    email VARCHAR(100) UNIQUE
);
```

### 2. ALTER TABLE

**Definition**: Modifies an existing table structure by adding, modifying, or dropping columns.

**Syntax**:
```sql
ALTER TABLE table_name
ADD COLUMN column_name data_type [constraints];

ALTER TABLE table_name
MODIFY COLUMN column_name data_type [constraints];

ALTER TABLE table_name
DROP COLUMN column_name;
```

**Examples**:
```sql
-- Add a new column
ALTER TABLE employees
ADD COLUMN phone_number VARCHAR(15);

-- Modify a column
ALTER TABLE employees
MODIFY COLUMN salary DECIMAL(12, 2);

-- Drop a column
ALTER TABLE employees
DROP COLUMN phone_number;
```

### 3. DROP TABLE

**Definition**: Deletes a table and all its data from the database.

**Syntax**:
```sql
DROP TABLE table_name;
DROP TABLE IF EXISTS table_name;
```

**Example**:
```sql
DROP TABLE IF EXISTS employees;
```

### 4. TRUNCATE TABLE

**Definition**: Removes all rows from a table but keeps the table structure intact.

**Syntax**:
```sql
TRUNCATE TABLE table_name;
```

**Example**:
```sql
TRUNCATE TABLE employees;
```

### 5. CREATE DATABASE

**Definition**: Creates a new database.

**Syntax**:
```sql
CREATE DATABASE database_name;
CREATE DATABASE IF NOT EXISTS database_name;
```

**Example**:
```sql
CREATE DATABASE company_db;
```

### 6. DROP DATABASE

**Definition**: Deletes an entire database with all its tables.

**Syntax**:
```sql
DROP DATABASE database_name;
DROP DATABASE IF EXISTS database_name;
```

**Example**:
```sql
DROP DATABASE company_db;
```

### 7. RENAME TABLE

**Definition**: Renames an existing table.

**Syntax**:
```sql
RENAME TABLE old_name TO new_name;
ALTER TABLE old_name RENAME TO new_name;
```

**Example**:
```sql
RENAME TABLE employees TO staff;
```

---

## DML Commands

DML (Data Manipulation Language) commands are used to manage data in the database.

### 1. INSERT

**Definition**: Adds new rows/records to a table.

**Syntax**:
```sql
INSERT INTO table_name (column1, column2, column3, ...)
VALUES (value1, value2, value3, ...);

INSERT INTO table_name
VALUES (value1, value2, value3, ...);

INSERT INTO table_name (column_list)
SELECT column_list FROM another_table;
```

**Examples**:
```sql
-- Insert single row
INSERT INTO employees (emp_name, department, salary, hire_date)
VALUES ('John Doe', 'IT', 50000, '2023-01-15');

-- Insert multiple rows
INSERT INTO employees (emp_name, department, salary)
VALUES 
('Jane Smith', 'HR', 45000),
('Mike Johnson', 'Sales', 55000),
('Sarah Williams', 'IT', 52000);

-- Insert from another table
INSERT INTO employees_archive
SELECT * FROM employees WHERE hire_date < '2020-01-01';
```

### 2. SELECT

**Definition**: Retrieves data from one or more tables.

**Syntax**:
```sql
SELECT column1, column2, ...
FROM table_name
WHERE condition
GROUP BY column_name
HAVING condition
ORDER BY column_name [ASC|DESC]
LIMIT number;
```

**Examples**:
```sql
-- Simple select
SELECT emp_name, salary FROM employees;

-- With WHERE clause
SELECT * FROM employees WHERE salary > 50000;

-- With GROUP BY and HAVING
SELECT department, COUNT(*) AS emp_count, AVG(salary) AS avg_salary
FROM employees
GROUP BY department
HAVING COUNT(*) > 2;

-- With ORDER BY
SELECT * FROM employees ORDER BY salary DESC;

-- With LIMIT
SELECT * FROM employees LIMIT 5;
```

### 3. UPDATE

**Definition**: Modifies existing data in a table.

**Syntax**:
```sql
UPDATE table_name
SET column1 = value1, column2 = value2, ...
WHERE condition;
```

**Examples**:
```sql
-- Update single row
UPDATE employees
SET salary = 55000
WHERE emp_id = 1;

-- Update multiple columns
UPDATE employees
SET salary = 60000, department = 'Management'
WHERE emp_name = 'John Doe';

-- Update based on condition
UPDATE employees
SET salary = salary * 1.1
WHERE department = 'IT';
```

### 4. DELETE

**Definition**: Removes rows from a table.

**Syntax**:
```sql
DELETE FROM table_name
WHERE condition;
```

**Examples**:
```sql
-- Delete single record
DELETE FROM employees WHERE emp_id = 5;

-- Delete multiple records
DELETE FROM employees WHERE department = 'Sales' AND salary < 40000;

-- Delete all records (keeps table structure)
DELETE FROM employees;
```

---

## DCL Commands

DCL (Data Control Language) commands manage user permissions and access control.

### 1. GRANT

**Definition**: Assigns privileges to users.

**Syntax**:
```sql
GRANT privilege_list ON database.table
TO 'username'@'host' [IDENTIFIED BY 'password'];
```

**Examples**:
```sql
-- Grant all privileges
GRANT ALL PRIVILEGES ON company_db.* TO 'user1'@'localhost';

-- Grant specific privileges
GRANT SELECT, INSERT, UPDATE ON company_db.employees TO 'user2'@'%';

-- Grant on specific table
GRANT SELECT ON company_db.employees TO 'readonly_user'@'192.168.1.%';
```

### 2. REVOKE

**Definition**: Removes privileges from users.

**Syntax**:
```sql
REVOKE privilege_list ON database.table
FROM 'username'@'host';
```

**Examples**:
```sql
-- Revoke all privileges
REVOKE ALL PRIVILEGES ON company_db.* FROM 'user1'@'localhost';

-- Revoke specific privileges
REVOKE INSERT, UPDATE ON company_db.employees FROM 'user2'@'%';
```

---

## DTL/TCL Commands

TCL (Transaction Control Language) manages transactions in the database.

### 1. BEGIN/START TRANSACTION

**Definition**: Starts a new transaction.

**Syntax**:
```sql
BEGIN;
START TRANSACTION;
```

**Example**:
```sql
BEGIN;
UPDATE employees SET salary = salary - 5000 WHERE emp_id = 1;
UPDATE employees SET salary = salary + 5000 WHERE emp_id = 2;
```

### 2. COMMIT

**Definition**: Permanently saves all changes made in the current transaction.

**Syntax**:
```sql
COMMIT;
```

**Example**:
```sql
BEGIN;
UPDATE accounts SET balance = balance - 500 WHERE account_id = 1;
UPDATE accounts SET balance = balance + 500 WHERE account_id = 2;
COMMIT;
```

### 3. ROLLBACK

**Definition**: Undoes all changes made in the current transaction.

**Syntax**:
```sql
ROLLBACK;
ROLLBACK TO savepoint_name;
```

**Examples**:
```sql
-- Complete rollback
BEGIN;
DELETE FROM employees WHERE emp_id = 10;
ROLLBACK; -- Changes are undone

-- Rollback to savepoint
BEGIN;
UPDATE employees SET salary = 50000 WHERE emp_id = 1;
SAVEPOINT sp1;
UPDATE employees SET salary = 60000 WHERE emp_id = 2;
ROLLBACK TO sp1; -- Only second update is undone
COMMIT;
```

### 4. SAVEPOINT

**Definition**: Creates a marker within a transaction to rollback to.

**Syntax**:
```sql
SAVEPOINT savepoint_name;
```

**Example**:
```sql
BEGIN;
INSERT INTO employees VALUES (100, 'Alice', 'IT', 50000, '2023-01-01');
SAVEPOINT before_second_insert;
INSERT INTO employees VALUES (101, 'Bob', 'HR', 45000, '2023-02-01');

-- If something goes wrong with second insert:
ROLLBACK TO before_second_insert;
COMMIT;
```

---

## Aggregate Functions

Aggregate functions perform calculations on sets of values and return a single scalar value.

### 1. COUNT()

**Definition**: Returns the number of rows matching a criteria.

**Syntax**:
```sql
SELECT COUNT(*) FROM table_name;
SELECT COUNT(column_name) FROM table_name;
SELECT COUNT(DISTINCT column_name) FROM table_name;
```

**Examples**:
```sql
-- Count all rows
SELECT COUNT(*) AS total_employees FROM employees;

-- Count non-NULL values
SELECT COUNT(email) AS employees_with_email FROM employees;

-- Count distinct departments
SELECT COUNT(DISTINCT department) AS total_departments FROM employees;

-- Count with condition
SELECT COUNT(*) FROM employees WHERE salary > 50000;
```

### 2. SUM()

**Definition**: Returns the sum of numeric values.

**Syntax**:
```sql
SELECT SUM(column_name) FROM table_name;
```

**Examples**:
```sql
-- Calculate total salary
SELECT SUM(salary) AS total_payroll FROM employees;

-- Sum with condition
SELECT SUM(salary) FROM employees WHERE department = 'IT';

-- Sum with GROUP BY
SELECT department, SUM(salary) AS dept_payroll
FROM employees
GROUP BY department;
```

### 3. AVG()

**Definition**: Returns the average of numeric values.

**Syntax**:
```sql
SELECT AVG(column_name) FROM table_name;
```

**Examples**:
```sql
-- Calculate average salary
SELECT AVG(salary) AS avg_salary FROM employees;

-- Average salary by department
SELECT department, AVG(salary) AS avg_salary
FROM employees
GROUP BY department;

-- Average with condition
SELECT AVG(salary) FROM employees WHERE experience > 5;
```

### 4. MAX()

**Definition**: Returns the maximum value.

**Syntax**:
```sql
SELECT MAX(column_name) FROM table_name;
```

**Examples**:
```sql
-- Highest salary
SELECT MAX(salary) AS highest_salary FROM employees;

-- Highest salary by department
SELECT department, MAX(salary) AS highest_dept_salary
FROM employees
GROUP BY department;

-- Highest salary among a condition
SELECT MAX(salary) FROM employees WHERE department = 'IT';
```

### 5. MIN()

**Definition**: Returns the minimum value.

**Syntax**:
```sql
SELECT MIN(column_name) FROM table_name;
```

**Examples**:
```sql
-- Lowest salary
SELECT MIN(salary) AS lowest_salary FROM employees;

-- Lowest salary by department
SELECT department, MIN(salary) AS lowest_dept_salary
FROM employees
GROUP BY department;

-- Earliest hire date
SELECT MIN(hire_date) FROM employees;
```

### 6. GROUP_CONCAT()

**Definition**: Concatenates values from multiple rows into a single string.

**Syntax**:
```sql
SELECT GROUP_CONCAT(column_name) FROM table_name;
SELECT GROUP_CONCAT(column_name SEPARATOR ',') FROM table_name;
```

**Examples**:
```sql
-- Concatenate employee names
SELECT GROUP_CONCAT(emp_name) AS all_names FROM employees;

-- With custom separator
SELECT department, GROUP_CONCAT(emp_name SEPARATOR ' | ')
FROM employees
GROUP BY department;

-- With ORDER BY
SELECT GROUP_CONCAT(emp_name ORDER BY salary DESC)
FROM employees;
```

### 7. STDDEV() / STDDEV_POP()

**Definition**: Returns the standard deviation of values.

**Syntax**:
```sql
SELECT STDDEV(column_name) FROM table_name;
```

**Examples**:
```sql
SELECT STDDEV(salary) AS salary_stddev FROM employees;
```

### 8. VARIANCE()

**Definition**: Returns the variance of values.

**Syntax**:
```sql
SELECT VARIANCE(column_name) FROM table_name;
```

**Examples**:
```sql
SELECT VARIANCE(salary) AS salary_variance FROM employees;
```

---

## String Functions & Substring

String functions manipulate text values in MySQL.

### 1. SUBSTRING() / SUBSTR()

**Definition**: Extracts a portion of a string.

**Syntax**:
```sql
SUBSTRING(string, start_position, length)
SUBSTRING(string, start_position)
SUBSTR(string, start_position, length)
```

**Examples**:
```sql
-- Extract first 5 characters
SELECT SUBSTRING('Hello World', 1, 5);  -- Result: 'Hello'

-- Extract from position 7 onwards
SELECT SUBSTRING('Hello World', 7);  -- Result: 'World'

-- Extract from employee name
SELECT emp_name, SUBSTRING(emp_name, 1, 3) AS initial_3_chars
FROM employees;

-- Negative position (from end)
SELECT SUBSTRING('Hello World', -5, 5);  -- Result: 'World'
```

### 2. CONCAT()

**Definition**: Concatenates multiple strings.

**Syntax**:
```sql
CONCAT(string1, string2, string3, ...)
```

**Examples**:
```sql
-- Concatenate strings
SELECT CONCAT('Hello', ' ', 'World');  -- Result: 'Hello World'

-- Combine columns
SELECT CONCAT(emp_name, ' - ', department) AS emp_info
FROM employees;

-- Multiple concatenations
SELECT CONCAT(emp_name, ' (', emp_id, ') - ', salary)
FROM employees;
```

### 3. CONCAT_WS()

**Definition**: Concatenates strings with a separator.

**Syntax**:
```sql
CONCAT_WS(separator, string1, string2, ...)
```

**Examples**:
```sql
-- Concatenate with separator
SELECT CONCAT_WS(', ', 'John', 'Doe', 'IT');  -- Result: 'John, Doe, IT'

-- Create formatted address
SELECT CONCAT_WS(', ', emp_name, department, city)
FROM employees;
```

### 4. LENGTH() / CHAR_LENGTH()

**Definition**: Returns the length of a string.

**Syntax**:
```sql
LENGTH(string)
CHAR_LENGTH(string)
```

**Examples**:
```sql
-- Get string length
SELECT LENGTH('Hello');  -- Result: 5

-- Find employees with long names (more than 10 characters)
SELECT emp_name FROM employees WHERE LENGTH(emp_name) > 10;
```

### 5. UPPER() / LOWER()

**Definition**: Converts string to uppercase or lowercase.

**Syntax**:
```sql
UPPER(string)
LOWER(string)
```

**Examples**:
```sql
-- Convert to uppercase
SELECT UPPER('Hello');  -- Result: 'HELLO'

-- Convert to lowercase
SELECT LOWER('Hello');  -- Result: 'hello'

-- Case-insensitive search
SELECT * FROM employees WHERE LOWER(emp_name) = 'john doe';
```

### 6. TRIM() / LTRIM() / RTRIM()

**Definition**: Removes leading/trailing spaces.

**Syntax**:
```sql
TRIM(string)
LTRIM(string)
RTRIM(string)
```

**Examples**:
```sql
-- Remove spaces from both ends
SELECT TRIM('  Hello World  ');  -- Result: 'Hello World'

-- Remove leading spaces
SELECT LTRIM('  Hello  ');  -- Result: 'Hello  '

-- Remove trailing spaces
SELECT RTRIM('  Hello  ');  -- Result: '  Hello'

-- Clean employee names
SELECT TRIM(emp_name) FROM employees;
```

### 7. REPLACE()

**Definition**: Replaces all occurrences of a substring.

**Syntax**:
```sql
REPLACE(string, find_string, replace_string)
```

**Examples**:
```sql
-- Replace text
SELECT REPLACE('Hello World', 'World', 'MySQL');  -- Result: 'Hello MySQL'

-- Replace in column
SELECT REPLACE(email, '@oldomain.com', '@newdomain.com')
FROM employees;
```

### 8. INSTR()

**Definition**: Returns the position of a substring.

**Syntax**:
```sql
INSTR(string, find_string)
```

**Examples**:
```sql
-- Find position
SELECT INSTR('Hello World', 'World');  -- Result: 7

-- Check if substring exists
SELECT * FROM employees
WHERE INSTR(email, '@company.com') > 0;
```

### 9. LEFT() / RIGHT()

**Definition**: Extracts characters from left or right side.

**Syntax**:
```sql
LEFT(string, length)
RIGHT(string, length)
```

**Examples**:
```sql
-- Get first 3 characters
SELECT LEFT('Hello World', 3);  -- Result: 'Hel'

-- Get last 5 characters
SELECT RIGHT('Hello World', 5);  -- Result: 'World'

-- Extract domain from email
SELECT LEFT(email, INSTR(email, '@') - 1) AS username
FROM employees;
```

### 10. REPEAT()

**Definition**: Repeats a string multiple times.

**Syntax**:
```sql
REPEAT(string, count)
```

**Examples**:
```sql
SELECT REPEAT('*', 5);  -- Result: '*****'
SELECT REPEAT(emp_name, 2) FROM employees;
```

### 11. REVERSE()

**Definition**: Reverses a string.

**Syntax**:
```sql
REVERSE(string)
```

**Examples**:
```sql
SELECT REVERSE('Hello');  -- Result: 'olleH'
```

---

## Predefined Methods & Built-in Functions

### Date and Time Functions

### 1. NOW() / CURRENT_TIMESTAMP()

**Definition**: Returns the current date and time.

**Examples**:
```sql
SELECT NOW();  -- Result: 2024-05-18 14:30:45
SELECT CURRENT_TIMESTAMP();
```

### 2. DATE()

**Definition**: Extracts the date part from a datetime.

**Examples**:
```sql
SELECT DATE(NOW());  -- Result: 2024-05-18
SELECT DATE(hire_date) FROM employees;
```

### 3. TIME()

**Definition**: Extracts the time part from a datetime.

**Examples**:
```sql
SELECT TIME(NOW());  -- Result: 14:30:45
```

### 4. YEAR() / MONTH() / DAY()

**Definition**: Extracts year, month, or day from a date.

**Examples**:
```sql
SELECT YEAR('2024-05-18');     -- Result: 2024
SELECT MONTH('2024-05-18');    -- Result: 5
SELECT DAY('2024-05-18');      -- Result: 18

-- Get employees hired in 2023
SELECT * FROM employees WHERE YEAR(hire_date) = 2023;
```

### 5. DATE_ADD() / DATE_SUB()

**Definition**: Adds or subtracts a time interval from a date.

**Examples**:
```sql
-- Add 30 days
SELECT DATE_ADD(NOW(), INTERVAL 30 DAY);

-- Subtract 1 month
SELECT DATE_SUB(NOW(), INTERVAL 1 MONTH);

-- Add 1 year
SELECT DATE_ADD(hire_date, INTERVAL 1 YEAR) FROM employees;
```

### 6. DATEDIFF()

**Definition**: Returns the number of days between two dates.

**Examples**:
```sql
-- Days between today and hire date
SELECT DATEDIFF(NOW(), hire_date) AS days_employed
FROM employees;

-- Days until end of year
SELECT DATEDIFF('2024-12-31', NOW());
```

### 7. DATE_FORMAT()

**Definition**: Formats a date according to a specified format.

**Examples**:
```sql
-- Format date
SELECT DATE_FORMAT(NOW(), '%Y-%m-%d');      -- 2024-05-18
SELECT DATE_FORMAT(NOW(), '%d/%m/%Y');      -- 18/05/2024
SELECT DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:%s');  -- 2024-05-18 14:30:45

-- Format hire dates
SELECT emp_name, DATE_FORMAT(hire_date, '%d-%b-%Y')
FROM employees;
```

### Numeric Functions

### 1. ROUND()

**Definition**: Rounds a number to a specified number of decimal places.

**Examples**:
```sql
SELECT ROUND(123.456);      -- Result: 123
SELECT ROUND(123.456, 2);   -- Result: 123.46
SELECT ROUND(123.456, 1);   -- Result: 123.5
```

### 2. CEIL() / CEILING()

**Definition**: Rounds up to the nearest integer.

**Examples**:
```sql
SELECT CEIL(123.2);     -- Result: 124
SELECT CEILING(123.2);  -- Result: 124
```

### 3. FLOOR()

**Definition**: Rounds down to the nearest integer.

**Examples**:
```sql
SELECT FLOOR(123.9);    -- Result: 123
```

### 4. ABS()

**Definition**: Returns the absolute (positive) value.

**Examples**:
```sql
SELECT ABS(-50);        -- Result: 50
SELECT ABS(-123.45);    -- Result: 123.45
```

### 5. RAND()

**Definition**: Generates a random number between 0 and 1.

**Examples**:
```sql
SELECT RAND();                          -- Random decimal
SELECT FLOOR(RAND() * 100);             -- Random number 0-99
SELECT * FROM employees ORDER BY RAND() LIMIT 5;  -- Random 5 employees
```

### 6. POWER()

**Definition**: Raises a number to a power.

**Examples**:
```sql
SELECT POWER(2, 3);     -- Result: 8
SELECT POWER(5, 2);     -- Result: 25
```

### 7. SQRT()

**Definition**: Returns the square root.

**Examples**:
```sql
SELECT SQRT(16);        -- Result: 4
SELECT SQRT(25);        -- Result: 5
```

### Conditional Functions

### 1. IF()

**Definition**: Returns one value if condition is true, another if false.

**Examples**:
```sql
SELECT IF(salary > 50000, 'High', 'Low') AS salary_level
FROM employees;

SELECT emp_name, IF(department = 'IT', 'Tech', 'Non-Tech')
FROM employees;
```

### 2. CASE

**Definition**: More complex conditional logic.

**Examples**:
```sql
SELECT emp_name,
       CASE 
           WHEN salary > 60000 THEN 'Executive'
           WHEN salary > 50000 THEN 'Senior'
           WHEN salary > 40000 THEN 'Mid-level'
           ELSE 'Junior'
       END AS salary_grade
FROM employees;
```

### 3. COALESCE()

**Definition**: Returns the first non-NULL value.

**Examples**:
```sql
SELECT COALESCE(phone_number, email, 'No contact') AS contact
FROM employees;
```

### 4. NULLIF()

**Definition**: Returns NULL if two expressions are equal, otherwise returns the first expression.

**Examples**:
```sql
SELECT NULLIF(salary, 50000) FROM employees;
```

---

## Indexes

Indexes improve query performance by reducing the amount of data the database needs to scan.

### 1. CREATE INDEX

**Definition**: Creates an index on one or more columns.

**Syntax**:
```sql
CREATE INDEX index_name ON table_name (column_name);
CREATE INDEX index_name ON table_name (column1, column2, ...);
CREATE UNIQUE INDEX index_name ON table_name (column_name);
```

**Examples**:
```sql
-- Single column index
CREATE INDEX idx_emp_name ON employees (emp_name);

-- Multiple column index (composite)
CREATE INDEX idx_dept_salary ON employees (department, salary);

-- Unique index
CREATE UNIQUE INDEX idx_email ON employees (email);
```

### 2. DROP INDEX

**Definition**: Removes an index.

**Syntax**:
```sql
DROP INDEX index_name ON table_name;
```

**Example**:
```sql
DROP INDEX idx_emp_name ON employees;
```

### 3. SHOW INDEXES

**Definition**: Displays all indexes on a table.

**Syntax**:
```sql
SHOW INDEXES FROM table_name;
SHOW INDEX FROM table_name;
DESCRIBE table_name;
```

**Example**:
```sql
SHOW INDEXES FROM employees;
```

### 4. PRIMARY KEY

**Definition**: Unique identifier for each record; implicit index.

**Example**:
```sql
CREATE TABLE employees (
    emp_id INT PRIMARY KEY AUTO_INCREMENT,
    emp_name VARCHAR(100)
);

-- Or add later
ALTER TABLE employees ADD PRIMARY KEY (emp_id);
```

### 5. UNIQUE INDEX

**Definition**: Ensures all values in the indexed columns are unique.

**Example**:
```sql
CREATE UNIQUE INDEX idx_email ON employees (email);

-- Alternative
ALTER TABLE employees ADD UNIQUE KEY uk_email (email);
```

### 6. FULLTEXT INDEX

**Definition**: Optimized for text searching.

**Example**:
```sql
CREATE FULLTEXT INDEX ft_description ON products (description);

-- Search using FULLTEXT
SELECT * FROM products 
WHERE MATCH(description) AGAINST('laptop' IN BOOLEAN MODE);
```

### Best Practices for Indexes

- **Index frequently searched columns**: WHERE, JOIN, ORDER BY, GROUP BY
- **Don't over-index**: Too many indexes slow down INSERT, UPDATE, DELETE
- **Use composite indexes for related queries**: Columns often used together
- **Monitor index performance**: Use EXPLAIN to see query execution plans

```sql
-- Check query execution plan
EXPLAIN SELECT * FROM employees WHERE emp_name = 'John' AND department = 'IT';
```

---

## Interview SQL Questions

### Basic Level

#### Q1: Find the second highest salary in the employees table.

```sql
SELECT MAX(salary) AS second_highest_salary
FROM employees
WHERE salary < (SELECT MAX(salary) FROM employees);

-- Alternative using LIMIT
SELECT DISTINCT salary FROM employees ORDER BY salary DESC LIMIT 1 OFFSET 1;
```

#### Q2: Find all employees who earn more than the average salary.

```sql
SELECT * FROM employees
WHERE salary > (SELECT AVG(salary) FROM employees);
```

#### Q3: Get the count of employees in each department.

```sql
SELECT department, COUNT(*) AS emp_count
FROM employees
GROUP BY department;
```

#### Q4: Find employees with salary between 40000 and 60000.

```sql
SELECT * FROM employees
WHERE salary BETWEEN 40000 AND 60000;
```

#### Q5: List all distinct departments.

```sql
SELECT DISTINCT department FROM employees;
```

### Intermediate Level

#### Q6: Find the department with the highest average salary.

```sql
SELECT department, AVG(salary) AS avg_salary
FROM employees
GROUP BY department
ORDER BY avg_salary DESC
LIMIT 1;
```

#### Q7: Get employees who have the same salary as another employee.

```sql
SELECT * FROM employees e1
WHERE salary IN (
    SELECT salary FROM employees e2
    WHERE e1.emp_id != e2.emp_id
);
```

#### Q8: Find the top 3 paid employees in each department.

```sql
SELECT emp_name, department, salary
FROM employees e1
WHERE (
    SELECT COUNT(*) FROM employees e2
    WHERE e1.department = e2.department
    AND e2.salary >= e1.salary
) <= 3
ORDER BY department, salary DESC;
```

#### Q9: Get employees hired in the last 6 months.

```sql
SELECT * FROM employees
WHERE hire_date >= DATE_SUB(NOW(), INTERVAL 6 MONTH);
```

#### Q10: Find employees whose names start with 'J'.

```sql
SELECT * FROM employees
WHERE emp_name LIKE 'J%';
```

### Advanced Level

#### Q11: Find duplicate email addresses in the employees table.

```sql
SELECT email, COUNT(*) as count
FROM employees
GROUP BY email
HAVING COUNT(*) > 1;
```

#### Q12: Get cumulative salary by department (running total).

```sql
SELECT emp_name, department, salary,
       SUM(salary) OVER (PARTITION BY department ORDER BY emp_id) AS running_total
FROM employees;
```

#### Q13: Find employees who earned a raise (appeared in multiple salary records).

```sql
-- Assuming a salary_history table
SELECT DISTINCT e1.emp_id, e1.emp_name
FROM employees e1
INNER JOIN salary_history sh1 ON e1.emp_id = sh1.emp_id
INNER JOIN salary_history sh2 ON e1.emp_id = sh2.emp_id
WHERE sh1.salary < sh2.salary;
```

#### Q14: Get the nth highest salary without using LIMIT.

```sql
-- For 5th highest salary
SELECT MAX(salary) AS fifth_highest
FROM employees
WHERE salary <= (
    SELECT DISTINCT salary FROM employees
    ORDER BY salary DESC
    LIMIT 1 OFFSET 4
);
```

#### Q15: Find employees with no subordinates (if there's a manager_id column).

```sql
SELECT * FROM employees e1
WHERE emp_id NOT IN (
    SELECT DISTINCT manager_id FROM employees WHERE manager_id IS NOT NULL
);
```

### Complex Scenarios

#### Q16: Create a hierarchical list of employees and their reporting structure.

```sql
SELECT emp_name, manager_id,
       LEVEL,
       CONCAT(REPEAT('  ', LEVEL-1), emp_name) AS hierarchy
FROM employees
START WITH manager_id IS NULL
CONNECT BY PRIOR emp_id = manager_id;
```

#### Q17: Calculate year-over-year salary growth by department.

```sql
SELECT 
    d1.department,
    YEAR(d1.hire_date) AS hire_year,
    COUNT(*) AS new_hires,
    AVG(d1.salary) AS avg_salary,
    ((AVG(d1.salary) - AVG(d2.salary)) / AVG(d2.salary) * 100) AS yoy_growth
FROM employees d1
LEFT JOIN employees d2 
    ON d1.department = d2.department 
    AND YEAR(d1.hire_date) = YEAR(d2.hire_date) + 1
GROUP BY d1.department, YEAR(d1.hire_date)
ORDER BY d1.department, hire_year;
```

#### Q18: Find the longest streak of employees hired consecutively without a break.

```sql
SELECT 
    MIN(hire_date) AS streak_start,
    MAX(hire_date) AS streak_end,
    COUNT(*) AS consecutive_hires
FROM (
    SELECT 
        hire_date,
        DATE_SUB(hire_date, INTERVAL ROW_NUMBER() OVER (ORDER BY hire_date) DAY) AS date_group
    FROM employees
) AS grouped
GROUP BY date_group
ORDER BY consecutive_hires DESC;
```

#### Q19: Find employees earning more than their manager.

```sql
SELECT e.emp_name, e.salary, m.emp_name AS manager_name, m.salary AS manager_salary
FROM employees e
INNER JOIN employees m ON e.manager_id = m.emp_id
WHERE e.salary > m.salary;
```

#### Q20: Get the department with the most employees earning above average salary.

```sql
SELECT department, COUNT(*) AS above_avg_count
FROM employees
WHERE salary > (SELECT AVG(salary) FROM employees)
GROUP BY department
ORDER BY above_avg_count DESC
LIMIT 1;
```

### Performance and Optimization

#### Q21: Find slow queries using EXPLAIN.

```sql
EXPLAIN SELECT e.*, d.dept_name
FROM employees e
LEFT JOIN departments d ON e.department = d.dept_id
WHERE e.salary > 50000
ORDER BY e.emp_name;

-- EXPLAIN output shows:
-- - Type of join operation
-- - Number of rows examined
-- - Using index or full table scan
```

#### Q22: Use indexing to improve query performance.

```sql
-- Create indexes for commonly queried columns
CREATE INDEX idx_salary ON employees (salary);
CREATE INDEX idx_dept_salary ON employees (department, salary);

-- Query becomes more efficient
SELECT * FROM employees 
WHERE department = 'IT' AND salary > 50000;
```

#### Q23: Optimize a query using JOIN vs Subquery.

```sql
-- Less efficient (subquery)
SELECT * FROM employees
WHERE emp_id IN (
    SELECT emp_id FROM orders GROUP BY emp_id
);

-- More efficient (JOIN)
SELECT DISTINCT e.* FROM employees e
INNER JOIN orders o ON e.emp_id = o.emp_id;
```

#### Q24: Using UNION instead of OR for better performance.

```sql
-- Less efficient (multiple ORs)
SELECT * FROM employees
WHERE department = 'IT' OR department = 'HR' OR department = 'Sales';

-- More efficient (UNION or IN)
SELECT * FROM employees
WHERE department IN ('IT', 'HR', 'Sales');

-- Or
SELECT * FROM employees WHERE department = 'IT'
UNION
SELECT * FROM employees WHERE department = 'HR'
UNION
SELECT * FROM employees WHERE department = 'Sales';
```

#### Q25: Find missing sequential IDs.

```sql
SELECT t1.emp_id + 1 AS missing_id
FROM employees t1
WHERE NOT EXISTS (
    SELECT 1 FROM employees t2
    WHERE t2.emp_id = t1.emp_id + 1
)
ORDER BY missing_id;
```

---

## Tips and Best Practices

1. **Use indices wisely** - Index columns used in WHERE, JOIN, and ORDER BY clauses
2. **Use EXPLAIN** - Analyze query execution plans
3. **Normalize your database** - Reduce data redundancy
4. **Use proper data types** - Use INT for integers, not VARCHAR
5. **Avoid SELECT *** - Specify only needed columns
6. **Use JOINs instead of subqueries** - Generally more efficient
7. **Batch operations** - Use INSERT multiple values instead of single inserts
8. **Use transactions** - Maintain data consistency
9. **Handle NULLs carefully** - Use IS NULL, not = NULL
10. **Regular maintenance** - Run OPTIMIZE TABLE and ANALYZE TABLE

---

## Common Mistakes to Avoid

1. **Forgetting WHERE clause in UPDATE/DELETE** - Will affect all rows!
   ```sql
   -- WRONG
   UPDATE employees SET salary = 50000;  -- All salaries set to 50000!
   
   -- CORRECT
   UPDATE employees SET salary = 50000 WHERE emp_id = 1;
   ```

2. **Using = instead of IS NULL**
   ```sql
   -- WRONG
   SELECT * FROM employees WHERE manager_id = NULL;
   
   -- CORRECT
   SELECT * FROM employees WHERE manager_id IS NULL;
   ```

3. **Not using table aliases in complex queries**
   ```sql
   -- Hard to read
   SELECT employees.emp_name FROM employees WHERE employees.salary > 50000;
   
   -- Better
   SELECT e.emp_name FROM employees e WHERE e.salary > 50000;
   ```

4. **Not backing up before DROP/TRUNCATE**
   ```sql
   -- Create backup first
   CREATE TABLE employees_backup AS SELECT * FROM employees;
   
   -- Then safe to truncate
   TRUNCATE TABLE employees;
   ```

5. **Forgetting to COMMIT in transactions**
   ```sql
   BEGIN;
   INSERT INTO employees VALUES (...);
   -- Don't forget this!
   COMMIT;
   ```

---

This comprehensive MySQL notes file covers all the essential topics for database management and optimization. Use these examples as reference for your daily database operations and SQL development!
