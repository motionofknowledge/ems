CREATE DATABASE employee_management;

USE employee_management;

CREATE TABLE employees (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    contact_no VARCHAR(20),
    salary DOUBLE,
    department VARCHAR(100)
);
