package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.EmployeeRequestDTO;
import com.example.employeemanagement.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeService {

    Employee createEmployee(EmployeeRequestDTO request);

    Employee getEmployeeById(Long id);

    List<Employee> getAllEmployees();

    Page<Employee> getAllEmployees(Pageable pageable);

    Employee updateEmployee(Long id, EmployeeRequestDTO request);

    void deleteEmployee(Long id);

    Page<Employee> searchEmployees(String name, Pageable pageable);

    Page<Employee> filterByDepartment(Long departmentId, Pageable pageable);

    Page<Employee> filterByDesignation(String designation, Pageable pageable);
}
