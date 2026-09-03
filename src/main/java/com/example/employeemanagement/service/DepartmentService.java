package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.DepartmentRequestDTO;
import com.example.employeemanagement.entity.Department;

import java.util.List;

public interface DepartmentService {

    Department createDepartment(DepartmentRequestDTO request);

    Department getDepartmentById(Long id);

    List<Department> getAllDepartments();

    Department updateDepartment(Long id, DepartmentRequestDTO request);

    void deleteDepartment(Long id);
}
