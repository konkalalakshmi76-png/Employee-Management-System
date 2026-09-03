package com.example.employeemanagement.service.impl;

import com.example.employeemanagement.dto.EmployeeRequestDTO;
import com.example.employeemanagement.entity.Department;
import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.exception.DepartmentNotFoundException;
import com.example.employeemanagement.exception.DuplicateEmployeeException;
import com.example.employeemanagement.exception.EmployeeNotFoundException;
import com.example.employeemanagement.exception.InvalidEmployeeException;
import com.example.employeemanagement.mapper.EmployeeMapper;
import com.example.employeemanagement.repository.DepartmentRepository;
import com.example.employeemanagement.repository.EmployeeRepository;
import com.example.employeemanagement.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeMapper employeeMapper;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository, EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.employeeMapper = employeeMapper;
    }

    @Override
    @Transactional
    public Employee createEmployee(EmployeeRequestDTO request) {
        validateEmployeeRequest(request);

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with id: " + request.getDepartmentId()));

        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmployeeException("Employee email already exists: " + request.getEmail());
        }

        if (employeeRepository.existsByEmployeeCode(request.getEmployeeCode())) {
            throw new DuplicateEmployeeException("Employee code already exists: " + request.getEmployeeCode());
        }

        Employee employee = employeeMapper.toEntity(request, department);
        Employee savedEmployee = employeeRepository.save(employee);
        log.info("Employee created successfully with code: {}", savedEmployee.getEmployeeCode());
        return savedEmployee;
    }

    @Override
    @Transactional(readOnly = true)
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Employee> getAllEmployees(Pageable pageable) {
        return employeeRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public Employee updateEmployee(Long id, EmployeeRequestDTO request) {
        Employee existing = getEmployeeById(id);
        validateEmployeeRequest(request);

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with id: " + request.getDepartmentId()));

        if (!existing.getEmail().equalsIgnoreCase(request.getEmail()) && employeeRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmployeeException("Employee email already exists: " + request.getEmail());
        }

        if (!existing.getEmployeeCode().equalsIgnoreCase(request.getEmployeeCode()) && employeeRepository.existsByEmployeeCode(request.getEmployeeCode())) {
            throw new DuplicateEmployeeException("Employee code already exists: " + request.getEmployeeCode());
        }

        employeeMapper.updateEntity(existing, request, department);
        Employee updatedEmployee = employeeRepository.save(existing);
        log.info("Employee updated successfully with id: {}", updatedEmployee.getId());
        return updatedEmployee;
    }

    @Override
    @Transactional
    public void deleteEmployee(Long id) {
        Employee employee = getEmployeeById(id);
        employeeRepository.delete(employee);
        log.info("Employee deleted successfully with id: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Employee> searchEmployees(String name, Pageable pageable) {
        if (name == null || name.trim().isEmpty()) {
            return employeeRepository.findAll(pageable);
        }
        return employeeRepository.searchByName(name.trim(), pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Employee> filterByDepartment(Long departmentId, Pageable pageable) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with id: " + departmentId));
        return employeeRepository.findByDepartment(department, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Employee> filterByDesignation(String designation, Pageable pageable) {
        if (designation == null || designation.trim().isEmpty()) {
            return employeeRepository.findAll(pageable);
        }
        return employeeRepository.findByDesignationContainingIgnoreCase(designation.trim(), pageable);
    }

    private void validateEmployeeRequest(EmployeeRequestDTO request) {
        if (request == null) {
            throw new InvalidEmployeeException("Employee request cannot be null");
        }
        if (request.getSalary() == null || request.getSalary().doubleValue() <= 0) {
            throw new InvalidEmployeeException("Salary must be greater than zero");
        }
        if (request.getDepartmentId() == null) {
            throw new InvalidEmployeeException("Department is required");
        }
    }
}
