CREATE DATABASE employee_management;

USE employee_management;

CREATE TABLE employees (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    contact_no VARCHAR(20),
    salary DOUBLE,
    department VARCHAR(100)
);

INSERT INTO employees (name, contact_no, salary, department) VALUES
    ('John Doe', '1234567890', 50000.00, 'IT'),
    ('Jane Smith', '9876543210', 60000.00, 'Sales'),
    ('Alice Johnson', '5555555555', 70000.00, 'Marketing');
