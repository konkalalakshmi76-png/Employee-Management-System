INSERT INTO departments (department_name, description, location) VALUES
('IT', 'Information Technology', 'Bengaluru'),
('HR', 'Human Resources', 'Hyderabad'),
('Finance', 'Finance and Accounting', 'Pune');

INSERT INTO addresses (street, city, state, pincode, country) VALUES
('12 MG Road', 'Bengaluru', 'Karnataka', '560001', 'India'),
('45 Jubilee Hills', 'Hyderabad', 'Telangana', '500033', 'India'),
('89 Baner Road', 'Pune', 'Maharashtra', '411045', 'India');

INSERT INTO employees (employee_code, first_name, last_name, email, phone, designation, salary, joining_date, status, department_id, address_id) VALUES
('EMP-1001', 'John', 'Doe', 'john.doe@example.com', '9876543210', 'Developer', 75000.00, '2024-01-10', 'ACTIVE', 1, 1),
('EMP-1002', 'Jane', 'Smith', 'jane.smith@example.com', '9123456789', 'HR Manager', 65000.00, '2023-09-15', 'ACTIVE', 2, 2),
('EMP-1003', 'Amit', 'Sharma', 'amit.sharma@example.com', '9988776655', 'Accountant', 70000.00, '2024-02-20', 'ACTIVE', 3, 3);
