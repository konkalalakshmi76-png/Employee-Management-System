package com.example.employeemanagement.service.impl;

import com.example.employeemanagement.dto.DepartmentRequestDTO;
import com.example.employeemanagement.entity.Department;
import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.exception.DepartmentNotFoundException;
import com.example.employeemanagement.exception.InvalidEmployeeException;
import com.example.employeemanagement.mapper.DepartmentMapper;
import com.example.employeemanagement.repository.DepartmentRepository;
import com.example.employeemanagement.repository.EmployeeRepository;
import com.example.employeemanagement.service.DepartmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private static final Logger log = LoggerFactory.getLogger(DepartmentServiceImpl.class);

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final DepartmentMapper departmentMapper;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository, EmployeeRepository employeeRepository, DepartmentMapper departmentMapper) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
        this.departmentMapper = departmentMapper;
    }

    @Override
    @Transactional
    public Department createDepartment(DepartmentRequestDTO request) {
        Department department = departmentMapper.toEntity(request);
        Department savedDepartment = departmentRepository.save(department);
        log.info("Department created successfully with name: {}", savedDepartment.getDepartmentName());
        return savedDepartment;
    }

    @Override
    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with id: " + id));
    }

    @Override
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @Override
    @Transactional
    public Department updateDepartment(Long id, DepartmentRequestDTO request) {
        Department department = getDepartmentById(id);
        departmentMapper.updateEntity(department, request);
        Department updatedDepartment = departmentRepository.save(department);
        log.info("Department updated successfully with id: {}", updatedDepartment.getId());
        return updatedDepartment;
    }

    @Override
    @Transactional
    public void deleteDepartment(Long id) {
        Department department = getDepartmentById(id);
        List<Employee> employees = employeeRepository.findByDepartmentId(id);
        if (!employees.isEmpty()) {
            throw new InvalidEmployeeException("Department cannot be deleted because employees are assigned to it");
        }
        departmentRepository.delete(department);
        log.info("Department deleted successfully with id: {}", id);
    }
}
